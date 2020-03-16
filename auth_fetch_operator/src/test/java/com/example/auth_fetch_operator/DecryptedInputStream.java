package com.example.auth_fetch_operator;


import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipException;

public class DecryptedInputStream extends InflaterInputStream {
    protected boolean usesDefaultInflater = true;
    protected CRC32 crc = new CRC32();

    /**
     * Indicates end of input stream.
     */
    protected boolean eos;

    private boolean closed = false;

    /**
     * Check to make sure that this stream has not been closed
     */
    private void ensureOpen() throws IOException {
        if (closed) {
            throw new IOException("Stream closed");
        }
    }

    public DecryptedInputStream(String data) throws IOException {
        this(data.getBytes("UTF-8"));
    }

    public DecryptedInputStream(byte[] data) throws IOException {
        super(new XInputStream(data), new Inflater(true), 512);
    }

    public int read(byte[] buf, int off, int len) throws IOException {
        ensureOpen();
        if (eos) {
            return -1;
        }
        int n = super.read(buf, off, len);
        if (n == -1) {
            if (readTrailer())
                eos = true;
            else
                return this.read(buf, off, len);
        } else {
            crc.update(buf, off, n);
        }
        return n;
    }

    /**
     * Closes this input stream and releases any system resources associated with
     * the stream.
     *
     * @exception IOException
     *                if an I/O error has occurred
     */
    public void close() throws IOException {
        if (!closed) {
            super.close();
            eos = true;
            closed = true;
        }
    }

    /**
     * encode header magic number.
     */
    public final static int encode_MAGIC = 0x8b1f;

    /*
     * File header flags.
     */
    private final static int FTEXT = 1; // Extra text
    private final static int FHCRC = 2; // Header CRC
    private final static int FEXTRA = 4; // Extra field
    private final static int FNAME = 8; // File name
    private final static int FCOMMENT = 16; // File comment

    /*
     * Reads encode member header and returns the total byte number of this member
     * header.
     */
    private int readHeader(InputStream this_in) throws IOException {
        CheckedInputStream in = new CheckedInputStream(this_in, crc);
        crc.reset();
        // Check header magic
        if (readUShort(in) != encode_MAGIC) {
            throw new ZipException("Not in encode format");
        }
        // Check compression method
        if (readUByte(in) != 8) {
            throw new ZipException("Unsupported compression method");
        }
        // Read flags
        int flg = readUByte(in);
        // Skip MTIME, XFL, and OS fields
        skipBytes(in, 6);
        int n = 2 + 2 + 6;
        // Skip optional extra field
        if ((flg & FEXTRA) == FEXTRA) {
            int m = readUShort(in);
            skipBytes(in, m);
            n += m + 2;
        }
        // Skip optional file name
        if ((flg & FNAME) == FNAME) {
            do {
                n++;
            } while (readUByte(in) != 0);
        }
        // Skip optional file comment
        if ((flg & FCOMMENT) == FCOMMENT) {
            do {
                n++;
            } while (readUByte(in) != 0);
        }
        // Check optional header CRC
        if ((flg & FHCRC) == FHCRC) {
            int v = (int) crc.getValue() & 0xffff;
            if (readUShort(in) != v) {
                throw new ZipException("Corrupt encode header");
            }
            n += 2;
        }
        crc.reset();
        return n;
    }

    /*
     * Reads encode member trailer and returns true if the eos reached, false if
     * there are more (concatenated gzip data set)
     */
    private boolean readTrailer() throws IOException {
        InputStream in = this.in;
        int n = inf.getRemaining();
        if (n > 0) {
            in = new SequenceInputStream(new ByteArrayInputStream(buf, len - n, n), new FilterInputStream(in) {
                public void close() throws IOException {
                }
            });
        }
        // Uses left-to-right evaluation order
        if ((readUInt(in) != crc.getValue()) ||
                // rfc1952; ISIZE is the input size modulo 2^32
                (readUInt(in) != (inf.getBytesWritten() & 0xffffffffL)))
            throw new IOException("Corrupt encode trailer");

        // If there are more bytes available in "in" or
        // the leftover in the "inf" is > 26 bytes:
        // this.trailer(8) + next.header.min(10) + next.trailer(8)
        // try concatenated case
        if (this.in.available() > 0 || n > 26) {
            int m = 8; // this.trailer
            try {
                m += readHeader(in); // next.header
            } catch (IOException ze) {
                return true; // ignore any malformed, do nothing
            }
            inf.reset();
            if (n > m)
                inf.setInput(buf, len - n + m, n - m);
            return false;
        }
        return true;
    }

    /*
     * Reads unsigned integer in Intel byte order.
     */
    private long readUInt(InputStream in) throws IOException {
        long s = readUShort(in);
        return ((long) readUShort(in) << 16) | s;
    }

    /*
     * Reads unsigned short in Intel byte order.
     */
    private int readUShort(InputStream in) throws IOException {
        int b = readUByte(in);
        return (readUByte(in) << 8) | b;
    }

    /*
     * Reads unsigned byte.
     */
    private int readUByte(InputStream in) throws IOException {
        int b = in.read();
        if (b == -1) {
            throw new EOFException();
        }
        if (b < -1 || b > 255) {
            // Report on this.in, not argument in; see read{Header, Trailer}.
            throw new IOException(this.in.getClass().getName() + ".read() returned value out of range -1..255: " + b);
        }
        return b;
    }

    private byte[] tmpbuf = new byte[128];

    /*
     * Skips bytes of input data blocking until all bytes are skipped. Does not
     * assume that the input stream is capable of seeking.
     */
    private void skipBytes(InputStream in, int n) throws IOException {
        while (n > 0) {
            int len = in.read(tmpbuf, 0, n < tmpbuf.length ? n : tmpbuf.length);
            if (len == -1) {
                throw new EOFException();
            }
            n -= len;
        }
    }

    static class XInputStream extends ByteArrayInputStream {
        public XInputStream(byte[] buf) {
            super(buf);
        }

        public synchronized int read(byte b[], int off, int len) {
            if (b == null) {
                throw new NullPointerException();
            } else if (off < 0 || len < 0 || len > b.length - off) {
                throw new IndexOutOfBoundsException();
            }

            if (pos >= count) {
                return -1;
            }

            int avail = count - pos;
            if (len > avail) {
                len = avail;
            }
            if (len <= 0) {
                return 0;
            }

            System.arraycopy(buf, pos, b, off, len);
            for (int i = 0; i < len; i++) {
                b[i] = (byte) (b[i] ^ 13);
            }
            pos += len;
            return len;
        }
    }
}
