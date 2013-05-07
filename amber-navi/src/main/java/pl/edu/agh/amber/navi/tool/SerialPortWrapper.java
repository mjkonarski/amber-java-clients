package pl.edu.agh.amber.navi.tool;

import gnu.io.SerialPort;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SerialPortWrapper {

    private final SerialPort sp;

    private final InputStream is;

    private final InputStreamReader isr;

    private final BufferedReader buf;

    public SerialPortWrapper(SerialPort sp, InputStream is, InputStreamReader isr, BufferedReader buf) {
        this.sp = sp;
        this.is = is;
        this.isr = isr;
        this.buf = buf;
    }

    public SerialPort getSp() {
        return sp;
    }

    public InputStream getIs() {
        return is;
    }

    public InputStreamReader getIsr() {
        return isr;
    }

    public BufferedReader getBuf() {
        return buf;
    }
}
