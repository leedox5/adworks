package mobis.mcams.ps.servlet;

import java.io.*;
import java.net.URLEncoder;

import javax.servlet.*;
import javax.servlet.http.*;

import mobis.common.base.*;
import mobis.common.util.*;
import mobis.mcams.common.*;

import mobis.mcams.ps.wb.*;

/**
 * @(#)JAR_PSL3_S.java
 * @작성 날짜: (2009-09-27)
 * @author: 나상모
 */
public class JAR_PSL3_S extends MOBISBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	/**
	 * doGet Overriding
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		try {
			this.performTask(req, res);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			//printExceptionPage(req, res, e);
		}
	}


	/**
	 * doPost Overriding
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		try {
			this.performTask(req, res);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			//printExceptionPage(req, res, e);
		}
	}


	/**
	 * Task 분기
	 */
	public void performTask(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException, Exception {
		RequestInfo reqInfo = ServletHelper.getRequestInfo(req);
		String COMH_FUNC = reqInfo.get("COMH_FUNC");

		if (COMH_FUNC.equals("") || COMH_FUNC.equals("IQ")) {
			performTest(req, res, reqInfo);
		} else if(COMH_FUNC.equals("XX")) {
			performJson(res);
		} else if(COMH_FUNC.equals("YY")) {
			performSave(req, res);
		} else {
			throw new ServletException("Invalid task [" + COMH_FUNC + "]");
		}
	}

	private void performSave(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PrintWriter out = res.getWriter();
		out.println(req.getParameter("data"));
	}


	private void performJson(HttpServletResponse res) throws IOException {
		PrintWriter out = res.getWriter();
		out.println("[{\"level\":\"2\",\"partno\":\"AAAAAAAAA\"},{\"level\":\"3\",\"partno\":\"BBBBBBBBB\"}]");
	}


	private void performTest(HttpServletRequest req, HttpServletResponse res,
			RequestInfo reqInfo) throws Exception {
		DataSet ds_user = null;
		String USERLANG = null;
		String USERID = null;
		String RNUMBER = null;
		String url = null;
		JAR_PSL1_W wb = new JAR_PSL1_W();
		try {
			// 사용자 ID
			USERID = reqInfo.get("USERID").equals("") ? "ENGNU62" : trim(reqInfo.get("USERID"));

			// 난수 OTP
			RNUMBER = reqInfo.get("RNUMBER").equals("") ? "1234" : trim(reqInfo.get("RNUMBER"));

			// 업무화면
			url = req.getParameter("URL");    // <== url만큼은 원본 그대로 읽어야 한다. (특수문자 많으므로)

			// 언어
			USERLANG = trim(reqInfo.get("USERLANG")).toUpperCase();

			// 언어코드 변환.
			if (USERLANG.equals("CHN")) {
				USERLANG = "ZH";
			} else if (USERLANG.equals("ENG")) {
				USERLANG = "EN";
			} else {
				USERLANG = "KO";
			}

			if (!USERID.equals("") && !RNUMBER.equals("")) {

				boolean isOTP = true;

				// OTP 인증이 성공한 경우
				if (isOTP == true) {

					ds_user = wb.accessLogin(USERID);
				    ds_user.set("EMPL_NUMB", USERID);
				    wb.getDefaultCorp(ds_user);

					ds_user.set("LANG_CODE", USERLANG);
					ds_user.set("MPOS_FLAG", "Y");

					JAR_PSL1_W.update_LAST_LGIN(req, USERID, USERLANG);
					wb.setSessionInfo(req, res, ds_user, 14400);
					McamsLoginHelper.setCookie(res, "MCAMS_SABN", USERID);
					McamsLoginHelper.setCookie(res, "MCAMS_CORP_CODE", ds_user.get("CORP_CODE"));
					McamsLoginHelper.setCookie(res, "MCAMS_CORP_ALL0", ds_user.get("CORP_ALL0"));
					McamsLoginHelper.setCookie(res, "MCAMS_CORP_NAME", URLEncoder.encode(ds_user.get("CORP_NAME"), "UTF-8"));

					// 로그인 ACC_LOG처리
					accessLogging(req, "PSL3", System.currentTimeMillis());
				} else {
					System.out.println("OTP CHECK FAILED. isOTP : " + isOTP);
				}
			} else {
				System.out.println("NO OTP DATA!");
			}

			url = "/mcams" + url;

			// html 기호 변환 처리
			url = StringUtils.replace(url, "?", "%3F");
			url = StringUtils.replace(url, "&", "%26");
			url = StringUtils.replace(url, "=", "%3D");

			/*
			PrintWriter out = res.getWriter();
			out.println("<script language='javascript'> \n");
			out.println("self.document.location.href = '/mcams/ps/JVR_PS33_V.jsp?COMC_TAGT_MENU=" + url + "'; \n");
			out.println("</script>                                                                            \n");
			*/
			url = "/ps/JVR_PS33_V.jsp?COMC_TAGT_MENU=" + url ;
			forward(url, req, res);
		} finally {

		}		
	}


	private String trim(String s) {
		return s == null ? "" : s.trim();
	}



	/**
	 * OTP 로그인
	 */
	public void performLogin(HttpServletRequest req, HttpServletResponse res, RequestInfo reqInfo) throws ServletException, IOException, Exception {
		DataSet ds_user = null;
		String USERLANG = null;
		String USERID = null;
		String RNUMBER = null;
		String url = null;
		JAR_PSL1_W wb = new JAR_PSL1_W();
		JAR_PSL3_W wb_PSL3 = new JAR_PSL3_W();

		try {
			// 사용자 ID
			USERID = trim(reqInfo.get("USERID"));

			// 난수 OTP
			RNUMBER = trim(reqInfo.get("RNUMBER"));

			// 업무화면
			url = req.getParameter("URL");    // <== url만큼은 원본 그대로 읽어야 한다. (특수문자 많으므로)

			// 언어
			USERLANG = trim(reqInfo.get("USERLANG")).toUpperCase();

			// 언어코드 변환.
			if (USERLANG.equals("CHN")) {
				USERLANG = "ZH";
			} else if (USERLANG.equals("ENG")) {
				USERLANG = "EN";
			} else {
				USERLANG = "KO";
			}

			if (!USERID.equals("") && !RNUMBER.equals("")) {

				boolean isOTP = false;
				isOTP = wb_PSL3.isValidOTP(USERID, RNUMBER);

				// OTP 인증이 성공한 경우
				if (isOTP == true) {

					ds_user = wb.accessLogin(USERID);
				    ds_user.set("EMPL_NUMB", USERID);
				    wb.getDefaultCorp(ds_user);

					ds_user.set("LANG_CODE", USERLANG);
					ds_user.set("MPOS_FLAG", "Y");

					JAR_PSL1_W.update_LAST_LGIN(req, USERID, USERLANG);
					wb.setSessionInfo(req, res, ds_user, 14400);
					McamsLoginHelper.setCookie(res, "MCAMS_SABN", USERID);
					McamsLoginHelper.setCookie(res, "MCAMS_CORP_CODE", ds_user.get("CORP_CODE"));
					McamsLoginHelper.setCookie(res, "MCAMS_CORP_ALL0", ds_user.get("CORP_ALL0"));
					McamsLoginHelper.setCookie(res, "MCAMS_CORP_NAME", ds_user.get("CORP_NAME"));

					wb_PSL3.deleteOTP(USERID, RNUMBER);

					// 로그인 ACC_LOG처리
					accessLogging(req, "PSL3", System.currentTimeMillis());
				} else {
					System.out.println("OTP CHECK FAILED. isOTP : " + isOTP);
				}
			} else {
				System.out.println("NO OTP DATA!");
			}

			url = "/mcams" + url;

			// html 기호 변환 처리
			url = StringUtils.replace(url, "?", "%3F");
			url = StringUtils.replace(url, "&", "%26");
			url = StringUtils.replace(url, "=", "%3D");

			PrintWriter out = res.getWriter();
			out.println("<script language='javascript'> \n");
			out.println("self.document.location.href = '/mcams/ps/JVR_PS33_V.jsp?COMC_TAGT_MENU=" + url + "'; \n");
			out.println("</script>                                                                            \n");
		} finally {

		}
	}
}
