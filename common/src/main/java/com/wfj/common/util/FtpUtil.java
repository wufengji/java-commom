package com.wfj.common.util;

import com.wfj.common.exception.ServiceException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.SocketException;

/**
 * @author wfj
 * @data 2020/2/20
 */
public class FtpUtil {
    private static Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    public static String LOCAL_CHARSET = "GBK";

    public static String SERVER_CHARSET = "ISO-8859-1";

    /**
     * 获取FTPClient对象
     *
     * @param ftpHost     服务器IP
     * @param ftpPort     服务器端口号
     * @param ftpUserName 用户名
     * @param ftpPassword 密码
     * @return getFtpClient
     */
    public static FTPClient getFtpClient(String ftpHost, int ftpPort, String ftpUserName, String ftpPassword) {
        FTPClient ftp = null;
        try {
            ftp = new FTPClient();
            // 连接FPT服务器,设置IP及端口
            ftp.connect(ftpHost, ftpPort);
            // 设置用户名和密码
            ftp.login(ftpUserName, ftpPassword);
            // 设置连接超时时间,5000毫秒
            ftp.setConnectTimeout(50000);
            // 设置中文编码集，防止中文乱码
            // 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
            if (FTPReply.isPositiveCompletion(ftp.sendCommand(
                    "OPTS UTF8", "ON"))) {
                LOCAL_CHARSET = "UTF-8";
            }
            ftp.setControlEncoding(LOCAL_CHARSET);
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                logger.info("未连接到FTP，用户名或密码错误");
                ftp.disconnect();
            } else {
                logger.info("FTP连接成功");
            }
        } catch (SocketException e) {
            e.printStackTrace();
            logger.info("FTP的IP地址可能错误，请正确配置");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("FTP的端口错误,请正确配置");
        }
        return ftp;
    }

    /**
     * @param ftp
     * @param ftpPath        文件名开始/结束 中间统一/
     * @param fileName       utf-8
     * @param originFileName
     * @return
     */
    public static boolean uploadFile(FTPClient ftp, String ftpPath, String fileName, String originFileName) {
        boolean flag = false;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(originFileName));
            ///设置PassiveMode传输
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            //判断FPT目标文件夹时候存在不存在则创建
            if (!createDir(ftpPath, ftp)) {
                System.out.println("创建目录失败:" + ftpPath);
                throw new Exception("创建目录失败");
            }
            ftp.storeFile(new String(fileName.getBytes(LOCAL_CHARSET), SERVER_CHARSET), inputStream);
            flag = true;
        } catch (Exception e) {
            logger.info("上传失败:{}", e.getMessage());
            flag = false;
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("关闭流失败");
                }
            }
        }
        return flag;
    }

    /**
     * 退回主目录
     *
     * @param ftp
     * @param ftpPath /name开始结束 分隔符/
     * @return
     */
    public static Boolean returnMasterDir(FTPClient ftp, String ftpPath) {
        final String temp = "/";
        try {
            if (temp.equals(ftpPath)) {
                return true;
            }
            for (int i = 0; i < ftpPath.length() - ftpPath.replaceAll(temp, "").length(); i++) {
                ftp.changeToParentDirectory();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 关闭FTP方法
     *
     * @param ftp
     * @return
     */
    public static boolean closeFtp(FTPClient ftp) {
        try {
            ftp.logout();
            logger.info("关闭ftp");
        } catch (Exception e) {
            logger.error("FTP关闭失败");
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    logger.error("FTP关闭失败");
                }
            }
        }
        return false;
    }


    /**
     * 判断ftp服务器文件是否存在
     *
     * @param ftpClient
     * @param path
     * @return
     * @throws IOException
     */
    public boolean existFile(FTPClient ftpClient, String path) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        if (ftpFileArr.length > 0) {
            flag = true;
        }
        return flag;
    }

    /**
     * 创建目录
     *
     * @param remote
     * @param ftpClient
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static boolean createDir(String remote, FTPClient ftpClient) throws UnsupportedEncodingException, IOException {
        final String temp = "/";
        final String charsetNameGbk = "GBK";
        final String charsetNameIos = "iso-8859-1";

        boolean returnBoolean = true;
        remote = new String(remote.getBytes(LOCAL_CHARSET), SERVER_CHARSET);
        String directory = remote.substring(0, remote.lastIndexOf(temp) + 1);
        if (!directory.equalsIgnoreCase(temp) && !ftpClient.changeWorkingDirectory(new String(directory.getBytes(charsetNameGbk), charsetNameIos))) {
            int start = 0;
            int end = 0;
            if (directory.startsWith(temp)) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf(temp, start);
            while (true) {
                String subDirectory = remote.substring(start, end);
                if (!ftpClient.changeWorkingDirectory(subDirectory)) {
                    if (ftpClient.makeDirectory(subDirectory)) {
                        ftpClient.changeWorkingDirectory(subDirectory);
                    } else {
                        logger.error("创建目录失败");
                        returnBoolean = false;
                    }
                }
                start = end + 1;
                end = directory.indexOf(temp, start);
                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }
        return returnBoolean;
    }

    public static void main(String[] args) {
        FTPClient ftp = null;
        try {
            ftp = FtpUtil.getFtpClient("47.99.199.33", 21, "zl", "zlhk9999");
            FtpUtil.uploadFile(ftp, "EM6000/123/log/test/test/", "1.log", "C:\\Users\\wfj\\Desktop\\test\\1.log");
            returnMasterDir(ftp, "test/log/");
            FtpUtil.uploadFile(ftp, "EM6000/123/log/test/", "1.log", "C:\\Users\\wfj\\Desktop\\test\\1.log");
        } catch (Exception e) {
            throw new ServiceException("400", "无法访问远程服务器");
        } finally {
            if (ftp != null) {
                FtpUtil.closeFtp(ftp);
            }
        }
    }
}