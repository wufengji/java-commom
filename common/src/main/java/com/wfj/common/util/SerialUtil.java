package com.wfj.common.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import purejavacomm.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author wfj
 * @Description 控制板信息
 * @data 2020/4/20
 */
@Slf4j
public class SerialUtil implements SerialPortEventListener {

    public SerialPort serialPort = null;
    public InputStream in = null;
    public OutputStream out = null;
    private final static int BAUD_RATE = 19200;
    public BlockingQueue<byte[]> msgQueue = new LinkedBlockingQueue<byte[]>();

    /**
     * 获取所有设备所有串口名称
     *
     * @return
     */
    public static List<String> findPort() {
        // 获得当前所有可用串口
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        List<String> portNameList = new ArrayList<>();
        // 将可用串口名添加到List并返回该List
        CommPortIdentifier commPortIdentifier;
        try {
            while (portList.hasMoreElements()) {
                commPortIdentifier = ((CommPortIdentifier) portList.nextElement());
                if (commPortIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    portNameList.add(commPortIdentifier.getName());
                }
            }
        } catch (Exception e) {
            log.error("find port error:{}", e.getMessage());
        }
        return portNameList;
    }

    /**
     * 打开串口
     *
     * @param portName
     * @return
     */
    public synchronized boolean openPort(String portName) {
        boolean result = false;
        try {
            // 通过端口名识别端口
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            // 打开端口，并给端口名字和一个timeout（打开操作的超时时间）
            CommPort commPort = portIdentifier.open(portName, 2000);
            // 判断是不是串口
            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                // 设置一下串口的波特率等参数
                serialPort.setSerialPortParams(BAUD_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
                // 给当前串口添加一个监听器
                serialPort.addEventListener(this);
                // 设置监听器生效，即：当有数据时通知
                serialPort.notifyOnDataAvailable(true);
                this.serialPort = serialPort;
                in = serialPort.getInputStream();
                out = serialPort.getOutputStream();
                result = true;
            }
        } catch (Exception e) {
            log.error("open {} port error:{} ", portName, e.getMessage());
        }
        return result;
    }

    /**
     * 关闭串口
     */
    public void closeSerial() {
        if (msgQueue.isEmpty()) {
            msgQueue.clear();
        }
        if (null == this.serialPort) {
            return;
        }
        //热插拔时候串口丢失，直接关闭会出错
        if (!findPort().contains(this.serialPort.getName())) {
            this.serialPort = null;
            return;
        }
        try {
            serialPort.notifyOnDataAvailable(false);
            serialPort.removeEventListener();
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    log.error("关闭串口输出流失败：{}", e.getMessage());
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    log.error("关闭串口输入流失败：{}", e.getMessage());
                }
            }

            serialPort.close();
            //插拔之后关闭异常
        } catch (Exception e) {
            log.error("关闭串口出错", e.getMessage());
        }
        in = null;
        out = null;
        serialPort = null;
    }


    /**
     * addEventListener的方法,持续监听端口上是否有数据流
     */
    @Override
    public void serialEvent(SerialPortEvent event) {
        switch (event.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                readCom();
                break;
            default:
        }
    }

    /**
     * 读取串口数据
     */
    private void readCom() {
        if (this.serialPort == null || in == null) {
            return;
        }
        try {
            byte[] temp = null;
            while (in.available() > 0) {
                if (in.available() > 128) {
                    temp = new byte[128];
                } else {
                    temp = new byte[in.available()];
                }
                in.read(temp);
                msgQueue.add(temp);
            }
        } catch (IOException e) {
            log.error("读取串口数据异常：{}", e.getMessage());
        }
    }
}
