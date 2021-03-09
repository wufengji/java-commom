package com.wfj.common.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wfj
 * @since 2020/11/9
 */
public class ResponseHeaderUtil {
    private final static String EXCEL_TYPE = ".xlsx";

    /**
     * 返回Excel头信息
     *
     * @param fileName
     * @param response
     * @throws IOException
     */
    public static HttpServletResponse excelHeader(String fileName, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.addHeader("Content-disposition", "attachment;filename=" + fileName + EXCEL_TYPE);
        response.setHeader("Access-Control-Expose-Headers","Content-Disposition");
        return response;
    }
}
