package mobis.mcams.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import mobis.common.base.Database;
import mobis.common.base.LangMap;
import mobis.common.base.McamsConstant;
import mobis.common.util.BusinessException;
import mobis.common.util.DateUtils;
import mobis.common.util.Environment;
import mobis.common.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 원가에서만 사용하는 Util 모음
 */
public class McamsUtil {

	private static final Logger logger = LoggerFactory.getLogger(McamsUtil.class);

	/**
	 * application 변수 설정
	 */
	private static final String MCAMS_PGRM_INFO = "MCAMS_PGRM_INFO";


	/**
	 * Context Path
	 */
	private static String context_path = null;


	/**
	 * EXCEL 파일 생성시 사용되는 CSS
	 */
	protected static String DC_CSS_EXCEL = null;


	/**
	 * 공법명
	 */
	private static Map COME_NAME = null;


	/**
	 * 부문에 속하는 팀
	 */
	private static List<String> BUMN_R  = null; // 설계원가
	private static List<String> BUMN_D  = null; // 개발원가
	private static List<String> BUMN_S  = null; // AS원가
	private static List<String> BUMN_RR = null; // 설계담당
	private static List<String> BUMN_MY = null; // 영업담당
	private static List<String> BUMN_C  = null; // 계열사 - 전체(아래 3개)
	private static List<String> BUMN_CP = null; // 계열사 - 현대파워텍
	private static List<String> BUMN_CA = null; // 계열사 - 현대오토넷
	private static List<String> BUMN_CI = null; // 계열사 - IHL


	/**
	 * Environment 객체
	 */
	public static Environment ENVI = null;



	/**
	 * 실행 모드를 얻는다.
	 */
	public static String getRunningMode() {
		String mode = null;

		if (ENVI == null) {
			ENVI = Environment.getInstance();
		}

		mode = ENVI.get("running.mode");

		return mode;
	}



	/**
	 * 다국어 맵을 DB로 부터 얻어서 application객체에 저장한다.
	 */
	public LangMap setLangMapToApplication(ServletContext application, String MAP0_NAME) throws Exception {
		Connection con = null;
		Statement  stmt = null;
		ResultSet  rs   = null;
		StringBuffer sql = new StringBuffer();
		String LIKE = "";
		String LANG = "";
		LangMap map = new LangMap();

		try {
			if (MAP0_NAME.indexOf("_KO") != -1) {
				LANG = "KORN";
			} else if (MAP0_NAME.indexOf("_EN") != -1) {
				LANG = "ENGL";
			} else if (MAP0_NAME.indexOf("_ZH") != -1) {
				LANG = "CHIN";
			} else {
				throw new Exception("MAP0_NAME[" + MAP0_NAME + "]이 유효하지 않습니다.");
			}

			if (MAP0_NAME.startsWith("LABL_")) {
				LIKE = "'L%'";
			} else if (MAP0_NAME.startsWith("MESG_")) {
				LIKE = "'M%'";
			} else if (MAP0_NAME.startsWith("USER_")) {
				LIKE = "'U%' OR LANG_CODE LIKE 'D%' OR LANG_CODE LIKE 'POSI%' ";
			} else {
				throw new Exception("MAP0_NAME[" + MAP0_NAME + "]이 유효하지 않습니다.");
			}

			//con = DBConnMgr.getMcamsConnection();
			con = Database.getConnection();

			stmt = con.createStatement();

			sql.append("SELECT CODE                                                                    \n");
			sql.append("      ,NAME                                                                    \n");
			sql.append(" FROM (                                                                       \n");
			sql.append("       SELECT LANG_CODE  CODE                                                 \n");
			sql.append("             ,CASE WHEN LANG_NAME_" + LANG + " = 'ADD' THEN  LANG_NAME_KORN   \n");
			sql.append("                   ELSE LANG_NAME_" + LANG + "                                \n");
			sql.append("               END       NAME                                                 \n");
			sql.append("         FROM MCMFLE.PFRMLANG                                                 \n");
			sql.append("        WHERE LANG_CODE LIKE " + LIKE + "                                     \n");
			sql.append("          AND LANG_DELT_GUBN != 'Y'                                           \n");
			sql.append("       UNION                                                                  \n");
			sql.append("       SELECT 'U' || USER_EMPL_NUMB  CODE                                     \n");
			sql.append("             ,USER_NAME_" + LANG + " NAME                                     \n");
			sql.append("         FROM MCMFLE.LFRMUSER                                                 \n");
			sql.append("       UNION                                                                  \n");
			sql.append("       SELECT 'D' || DEPT_CODE       CODE                                     \n");
			sql.append("             ,DEPT_NAME_" + LANG + " NAME                                     \n");
			sql.append("         FROM MCMFLE.LFRMDEPT                                                 \n");
			sql.append("       UNION                                                                  \n");
			sql.append("       SELECT 'POSI' || POSI_CODE    CODE                                     \n");
			sql.append("             ,POSI_NAME_" + LANG + " NAME                                     \n");
			sql.append("         FROM MCMFLE.LFRMPOSI                                                 \n");
			sql.append("      ) A                                                                     \n");
			sql.append(" WHERE CODE > ' '                                                             \n");
			sql.append("  WITH UR                                                                     \n");

			rs = stmt.executeQuery(sql.toString());

			while (rs.next()) {
				map.set(rs.getString(1), rs.getString(2));
			}

			application.setAttribute(MAP0_NAME, map);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			System.out.println("# ERROR!!! SQL : \n" + sql.toString());
			throw new BusinessException("다국어-MAP 구성을 할 수 없습니다.");
		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
			//DBConnMgr.closeConnection(con);
			if(con != null) try { con.close(); } catch (SQLException e) {}
		}

		return map;
	}



	/**
	 * 공법명을 얻는다.
	 */
	public static String getCOME_NAME(HttpServletRequest req, String COME_CODE) throws Exception {
		String COMC_COME_NAME = "";
		String LANG = McamsLoginHelper.getLANG_CODE(req);

		Statement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		StringBuffer sql = null;

		if (COME_CODE == null) {
			return "";
		}

		// 처음인 경우
		// 각 언어별/법인별로 설정해야 한다.
		if (COME_NAME == null || COME_NAME.get(COME_CODE + "_" + LANG) == null) {

			if (COME_NAME == null) {
				COME_NAME = new HashMap();
			}

			sql = new StringBuffer();
			sql.append("SELECT DISTINCT COME_CODE                                            \n");
			sql.append("      ,COME_NAME   \n");
			sql.append("  FROM MCMFLE.PFRCCOME		                                         \n");
			sql.append("  WITH UR                                                            \n");

			try {

				con = DBConnMgr.getMcamsConnection();
				stmt = con.createStatement();
				rs = stmt.executeQuery(sql.toString());
				System.out.println(sql.toString());

				while (rs.next()) {
					COME_NAME.put(rs.getString("COME_CODE") + "_" + LANG, rs.getString("COME_NAME").trim());
				}
			} catch(Exception e) {
				System.out.println("# ERROR!!! SQL : \n" + sql.toString());
				throw e;
			} finally {
				DBConnMgr.closeResultSet(rs);
				DBConnMgr.closeStatement(stmt);
				DBConnMgr.closeConnection(con);
			}
		}

		COMC_COME_NAME = (String)COME_NAME.get(COME_CODE + "_" + LANG);
		return COMC_COME_NAME;
	}

	/**
	 * 공법명을 얻는다.
	 */
	public static String getCOME_NAME(HttpServletRequest req, Connection con, String COME_CODE) throws Exception {
		String COMC_COME_NAME = "";
		String LANG = McamsLoginHelper.getLANG_CODE(req);

		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = null;

		if (COME_CODE == null) {
			return "";
		}

		// 처음인 경우
		// 각 언어별/법인별로 설정해야 한다.
		if (COME_NAME == null || COME_NAME.get(COME_CODE + "_" + LANG) == null) {

			if (COME_NAME == null) {
				COME_NAME = new HashMap();
			}

			sql = new StringBuffer();
			sql.append("SELECT DISTINCT COME_CODE                                                     \n");
			sql.append("      ,CAST(MCMFLE.FNRMCMIQ02(COME_NAME_LNCD,'" + LANG + "') AS VARCHAR(100) CCSID 1208) COME_NAME   \n");
			sql.append("  FROM MCMFLE.PFRCCOME		                                         \n");
			sql.append("  WITH UR                                                            \n");

			try {
				stmt = con.createStatement();
				rs = stmt.executeQuery(sql.toString());

				while (rs.next()) {
					COME_NAME.put(rs.getString("COME_CODE") + "_" + LANG, rs.getString("COME_NAME").trim());
				}
			} catch(Exception e) {
				System.out.println("# ERROR!!! SQL : \n" + sql.toString());
				throw e;
			} finally {
				DBConnMgr.closeResultSet(rs);
				DBConnMgr.closeStatement(stmt);
			}
		}

		COMC_COME_NAME = (String)COME_NAME.get(COME_CODE + "_" + LANG);
		return COMC_COME_NAME;
	}

	/**
	 * 부문에 속한 팀들을 얻는다.
	 * 부문에 속한 팀을 추가/삭제시에는 FNRCCMIQ08.TXT도 같이 수정해야 한다.
	 */
	public static List<String> getBUMN_TEAM(String BUMN) {
		List<String> list = null;

		// 설계원가
		if (BUMN.equals("R")) {
			if (BUMN_R == null) {
				BUMN_R = new ArrayList<String>();
				BUMN_R.add("10152593");      // 1. 사업관리1팀
				BUMN_R.add("10001257");      // 2. 사업관리2팀
				BUMN_R.add("10002366");
				BUMN_R.add("10002632");      // 설계원가2팀
			}

			list = BUMN_R;
			// 개발원가
		} else if (BUMN.equals("D")) {
			if (BUMN_D == null) {
				BUMN_D = new ArrayList<String>();
				BUMN_D.add("00000000");      //  0. 임원진
				BUMN_D.add("19999999");      //  0. 그룹감사
				BUMN_D.add("10131007");      //  1. 감사1팀
				BUMN_D.add("10000835");      //  2. 하이테크개발팀
				BUMN_D.add("10120021");      //  3. 일반구매팀
				BUMN_D.add("10120029");      //  4. 의장모듈개발팀
				BUMN_D.add("10131015");      //  5. 제동/조향개발팀
				BUMN_D.add("10120023");      //  6. 구매기획팀
				BUMN_D.add("10120032");      //  7. 글로벌구매관리팀
				BUMN_D.add("10120028");      //  8. 샤시모듈개발팀
				BUMN_D.add("10000834");      //  9. 구매원가표준팀
				BUMN_D.add("10152614");      // 10. 메카부품개발팀
				BUMN_D.add("10152613");      // 11. 멀티부품개발팀

				BUMN_D.add("10001509");      // 00. 2011년도 추가 : 시스템부품개발팀
				BUMN_D.add("10001510");      // 00. 2011년도 추가 : 램프부품개발팀
				BUMN_D.add("10001493");      // 00. 2011년도 추가 : 전장모듈부품개발팀

				BUMN_D.add("10001896");      // 00. 2012년도 추가 : 램프부품구매팀
				BUMN_D.add("10001891");      // 00. 2012년도 추가 : 샤시모듈구매팀
				BUMN_D.add("10001893");      // 00. 2012년도 추가 : 시스템/친환경구매팀
				BUMN_D.add("10001895");      // 00. 2012년도 추가 : 의장모듈구매팀
				BUMN_D.add("10001899");      // 00. 2012년도 추가 : 전장기구부품구매팀
				BUMN_D.add("10001925");      // 00. 2012년도 추가 : 전장모듈구매팀
				BUMN_D.add("10001898");      // 00. 2012년도 추가 : 전장회로부품구매팀
				BUMN_D.add("10001892");      // 00. 2012년도 추가 : 제동/조향구매팀
				BUMN_D.add("10120024");      // 00. 2012년도 추가 : 해외구매팀
				BUMN_D.add("10001888");      // 00. 2012년도 추가 : 구매전략팀

				BUMN_D.add("10002542");      // 00. 2015년도 추가 : 구매시스템운영팀

				BUMN_D.add("10152614");      // 00. 전장기구부품개발팀 -- 기존팀 필요하다고 요청옴. 2012.03.22

				BUMN_D.add("10120108");      // 50. 북경모비스기차영부건
				BUMN_D.add("10120103");      // 51. 북경모비스중차
				BUMN_D.add("10120104");      // 52. 북경모비스변속기
				BUMN_D.add("10120107");      // 53. 강소모비스기차영부건
				BUMN_D.add("10120119");      // 53. 강소열달모비스
				BUMN_D.add("10120118");      // 54. 무석모비스
				BUMN_D.add("10120097");      // 55. 상해모비스
				BUMN_D.add("99999999");      // 56. 홍콩모비스
				BUMN_D.add("10152760");      // 57. 천진모비스

				BUMN_D.add("10120117");      // 60. 인도
				BUMN_D.add("10110011");      // 70. 미국-앨라바마
				BUMN_D.add("10000279");      // 71. 미국-조지아

				BUMN_D.add("10110015");      // 80. 유럽-슬로박
				BUMN_D.add("10000025");      // 81. 유럽-체코
			}

			list = BUMN_D;
			// AS원가
		} else if (BUMN.equals("S")) {
			if (BUMN_S == null) {
				BUMN_S = new ArrayList();
				BUMN_S.add("10120024");      // 1. 해외구매팀
				BUMN_S.add("10120025");      // 2. 부품구매지원팀
				BUMN_S.add("10120031");      // 3. 부품구매원가팀
				BUMN_S.add("10001185");      // 4. AM용품개발팀
			}


			list = BUMN_S;
			// 영업담당
		} else if (BUMN.equals("MY")) {
			if (BUMN_MY == null) {
				BUMN_MY = new ArrayList();
				BUMN_MY.add("10120125");     // 1. 모듈영업관리팀
				BUMN_MY.add("10120128");	 // 2. 제동/조향영업팀
				BUMN_MY.add("10001229");     // 3. 의장모듈영업팀
				BUMN_MY.add("10120131");     // 4. 모듈영업팀
				BUMN_MY.add("10000768");     // 5. 램프/안전영업팀
				BUMN_MY.add("10120127");     // 6. 글로벌부품영업팀
			}

			list = BUMN_MY;
			// 설계담당
		} else if (BUMN.equals("RR")) {
			if (BUMN_RR == null) {
				BUMN_RR = new ArrayList();
				BUMN_RR.add("10000537");	 //  1. 바디안전제어설계팀
				BUMN_RR.add("10131106");	 //  2. 안전전자설계팀
				BUMN_RR.add("10120150");	 //  3. 안전시스템설계팀
				BUMN_RR.add("10131088");	 //  4. 전자제어설계팀
				BUMN_RR.add("10120146");	 //  5. 샤시모듈설계팀
				BUMN_RR.add("10120147");	 //  6. 샤시부품설계팀
				BUMN_RR.add("10000536");	 //  7. 샤시전자설계팀
				BUMN_RR.add("10152557");	 //  8. 램프설계팀
				BUMN_RR.add("10120144");	 //  9. 의장모듈설계팀
				BUMN_RR.add("10120145");	 // 10. FEM설계팀
				BUMN_RR.add("10120148");	 // 11. 전자제동설계팀
				BUMN_RR.add("10120149");	 // 12. 제동설계팀
				BUMN_RR.add("10000801");	 // 13. 전력전자설계팀
			}

			list = BUMN_RR;
			// 계열사 - 전체
		} else if (BUMN.equals("C")) {
			if (BUMN_C == null) {
				BUMN_C = new ArrayList();
				BUMN_C.add("HKMC1005");
				BUMN_C.add("HKMC1006");
				BUMN_C.add("HKMC1007");
			}

			list = BUMN_C;
			// 계열사 - 현대파워텍
		} else if (BUMN.equals("CP")) {
			if (BUMN_CP == null) {
				BUMN_CP = new ArrayList();
				BUMN_CP.add("HKMC1005");
			}

			list = BUMN_CP;
			// 계열사 - IHL
		} else if (BUMN.equals("CI")) {
			if (BUMN_CI == null) {
				BUMN_CI = new ArrayList();
				BUMN_CI.add("HKMC1006");
			}

			list = BUMN_CI;
			// 계열사 - 현대오토넷
		} else if (BUMN.equals("CA")) {
			if (BUMN_CA == null) {
				BUMN_CA = new ArrayList();
				BUMN_CA.add("HKMC1007");
			}

			list = BUMN_CA;
		}



		return list;
	}



	/**
	 * 주어진 팀이 어느 부문에 속하는가?
	 */
	public static String getBUMN(String DEPT_CODE) throws Exception {
		String BUMN = null;
		List list = null;

		if (DEPT_CODE == null || DEPT_CODE.equals("")) {
			return "";
			// 협력업체의 경우
		} else if (DEPT_CODE.length() == 4) {
			return "DV";
		}


		// 설계원가
		list = getBUMN_TEAM("R");
		if (list.contains(DEPT_CODE) == true) {
			BUMN = "R";
			return BUMN;
		}

		// 개발원가
		list = getBUMN_TEAM("D");
		if (list.contains(DEPT_CODE) == true) {
			BUMN = "D";
			return BUMN;
		}

		// AS부문
		list = getBUMN_TEAM("S");
		if (list.contains(DEPT_CODE) == true) {
			BUMN = "S";
			return BUMN;
		}

		// 설계담당
		list = getBUMN_TEAM("RR");
		if (list.contains(DEPT_CODE) == true) {
			BUMN = "RR";
			return BUMN;
		}

		// 영업담당
		list = getBUMN_TEAM("MY");
		if (list.contains(DEPT_CODE) == true) {
			BUMN = "MY";
			return BUMN;
		}

		// 계열사 - 현대파워텍
		list = getBUMN_TEAM("CP");
		if (list.contains(DEPT_CODE) == true) {
			BUMN = "CP";
			return BUMN;
		}

		// 계열사 - 현대오토넷
		list = getBUMN_TEAM("CA");
		if (list.contains(DEPT_CODE) == true) {
			BUMN = "CA";
			return BUMN;
		}

		// 계열사 - IHL
		list = getBUMN_TEAM("CI");
		if (list.contains(DEPT_CODE) == true) {
			BUMN = "CI";
			return BUMN;
		}


		/****
		if (BUMN == null) {
			throw new Exception("어느 부문에도 속하지 않는 팀입니다.");
		}
		 ****/

		if (BUMN == null) {
			BUMN = "";
		}

		return BUMN;
	}



	/**
	 * context root path 얻기(물리적 경로)
	 * 예 : /was/apps/mcams
	 */
	public static String getContextPath(HttpServletRequest req) {

		if (context_path == null) {
			context_path = Environment.getInstance().get("mobis.mcams.context.root");
		}

		return context_path;
	}



	/**
	 * sql log path 얻기(물리적 경로)
	 * 예 : /was/apps/mcams/WEB-INF/sql
	 */
	public static String getSqlLogPath(HttpServletRequest req) {
		String path = null;
		path = getContextPath(req) + "/WEB-INF/src/mobis/mcams";

		return path;
	}




	/**
	 * EXCEL 파일 생성시 사용되는 CSS 얻기
	 */
	public static String getExcelCSS() {
		if (DC_CSS_EXCEL == null) {
			DC_CSS_EXCEL = new String();

			DC_CSS_EXCEL += ".number {                   \n";
			DC_CSS_EXCEL += "	mso-number-format: \\@   \n";
			DC_CSS_EXCEL += "}                           \n";
			DC_CSS_EXCEL += "                            \n";
			DC_CSS_EXCEL += "BODY {                      \n";
			DC_CSS_EXCEL += "	font-family: 바탕체;     \n";
			DC_CSS_EXCEL += "	font-size: 9pt;          \n";
			DC_CSS_EXCEL += "}                           \n";
			DC_CSS_EXCEL += "                            \n";
			DC_CSS_EXCEL += "TD {                        \n";
			DC_CSS_EXCEL += "	font-family: 바탕체;     \n";
			DC_CSS_EXCEL += "	font-size: 9pt;          \n";
			DC_CSS_EXCEL += "	vertical-align: middle;  \n";
			DC_CSS_EXCEL += "}                           \n";
			DC_CSS_EXCEL += "                            \n";
			DC_CSS_EXCEL += "TD.title {                  \n";
			DC_CSS_EXCEL += "	font-size: 14pt;         \n";
			DC_CSS_EXCEL += "	height: 30pt;            \n";
			DC_CSS_EXCEL += "}                           \n";
			DC_CSS_EXCEL += "                            \n";
			DC_CSS_EXCEL += "TH {                        \n";
			DC_CSS_EXCEL += "	font-family: 바탕체;     \n";
			DC_CSS_EXCEL += "	font-size: 9pt;          \n";
			DC_CSS_EXCEL += "	font-weight: normal;     \n";
			DC_CSS_EXCEL += "}                           \n";
			DC_CSS_EXCEL += "                            \n";
			DC_CSS_EXCEL += "TR {                        \n";
			DC_CSS_EXCEL += "	height: 15pt;            \n";
			DC_CSS_EXCEL += "}                           \n";
		}

		return DC_CSS_EXCEL;

	}


	/**
	 * 화폐 코드 얻기
	 * 예 : "U" => "USD", "E" => "EUR", "Y" => "YEN"
	 * 2011.06.22 HJJ : 인도, 한국추가
	 */
	public static Map MONY_CODE = null;

	public static String getMonyCode(String firstChar) {
		if (MONY_CODE == null) {
			MONY_CODE = new HashMap();

			MONY_CODE.put("Y", "YEN");
			MONY_CODE.put("U", "USD");
			MONY_CODE.put("E", "EUR");
			MONY_CODE.put("C", "CNY");
			MONY_CODE.put("M", "MRK");
			MONY_CODE.put("P", "PUS");
			MONY_CODE.put("F", "FRN");
			MONY_CODE.put("I", "INR");
			MONY_CODE.put("K", "KRW");
			MONY_CODE.put("B", "BRL");
			MONY_CODE.put("R", "RUB");
		}

		return (String)MONY_CODE.get(firstChar);
	}



	/**
	 * 저장/삭제 시 DEBUG INFO 함수 출력(Submit Jsp에서 사용)
	 */
	public static void printDebugInfo(javax.servlet.jsp.JspWriter out, String PRGM_ID, DataSet ds) throws Exception {
		out.println("parent.setDebugInfo(                    ");
		out.println("	\"" + PRGM_ID                  + "\",");
		out.println("	\"" + ds.get("COMT_MESG_CODE") + "\",");
		out.println("	\"" + StringUtils.replace(ds.get("COMT_MESG"),"\"","'").replaceAll("\\n", "\\\\n") + "\",");
		out.println("	\"" + ds.get("COMT_SQL0_CODE") + "\",");
		out.println("	\"" + ds.get("COMT_LINE_NO  ") + "\" ");
		out.println(");                                      ");
	}



	/**
	 * 중간에 스페이스가 들어간 PART NO를 얻는다.
	 */
	public static String getPartNo(String partNo) throws Exception {
		String newPartNo = "";

		if (partNo.length() >= 6) {
			newPartNo = partNo.substring(0, 5) + " " + partNo.substring(5);
		} else {
			newPartNo = partNo;
		}

		// 일단 임시로 DB값 그대로 넘긴다. 2010-03-25 12:04오후
		newPartNo = partNo;

		return newPartNo;
	}



	/**
	 * 사번으로 팀코드 얻기
	 */
	public static String getDEPT_CODE(String EMPL_NUMB) throws Exception {
		Connection con = null;

		con = DBConnMgr.getMcamsConnection();

		return getDEPT_CODE(con, EMPL_NUMB);
	}




	/**
	 * 사번으로 팀코드 얻기
	 */
	public static String getDEPT_CODE(Connection con, String EMPL_NUMB) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		String DEPT_CODE = "";

		try {
			sql.append("SELECT RTRIM(USER_DEPT_CODE)                \n");
			sql.append("  FROM MCMFLE.PFRMUSER                      \n");
			sql.append(" WHERE USER_EMPL_NUMB = '" + EMPL_NUMB + "' \n");
			sql.append("  WITH UR                                   \n");

			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				DEPT_CODE = rs.getString(1);
			}
		} catch(Exception e) {
			System.out.println("# ERROR!!! SQL : \n" + sql);
			throw e;
		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
		}

		return DEPT_CODE;
	}



	/**
	 * 재료명칭 가져오기
	 * 대상테이블 PFRBMATL
	 * INPUT : CONECTION CON, 표준구분, 재료코드, 적용일, 임시구분
	 * 2008.09.11 추가
	 */
	public static String getMATL_NAME(Connection con, String CORP_CODE, String MATL_STND_GUBN, String MATL_CODE, String MATL_APPL_FRDT, String MATL_GUBN) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		String MATL_NAME_KORN = "";

		try {
			sql.append("SELECT RTRIM(MATL_NAME_KORN)                     \n");
			sql.append("  FROM MCMFLE.PFRBMATL                           \n");
			sql.append(" WHERE MATL_CORP_CODE = '" + CORP_CODE      + "' \n");
			sql.append("   AND MATL_STND_GUBN = '" + MATL_STND_GUBN + "' \n");
			sql.append("   AND MATL_CODE      = '" + MATL_CODE + "'      \n");
			sql.append("   AND MATL_APPL_FRDT = '" + MATL_APPL_FRDT + "' \n");
			sql.append("   AND MATL_GUBN      = '" + MATL_GUBN + "'      \n");
			sql.append("  WITH UR                                        \n");

			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				MATL_NAME_KORN = rs.getString(1);
			}
		} catch(Exception e) {
			System.out.println("# sql : \n" + sql);
			throw e;
		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
		}

		return MATL_NAME_KORN;
	}


	/**
	 * 모델코드로 차종코드 얻기
	 */
	public static String getCarsCode(Connection con, String CORP_CODE, String MODL_CODE) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		String CARS_CODE = "";

		try {
			sql.append("SELECT CARS_CODE                                               \n");
			sql.append("      ,CARS_MODL_CODE                                          \n");
			sql.append("      ,CARS_MODL_NO                                            \n");
			sql.append("  FROM MCMFLE.LFRBCARS                                         \n");
			sql.append(" WHERE CARS_CORP_CODE = MCMFLE.FNRMCMIQ07('" + CORP_CODE + "') \n");
			sql.append("   AND CARS_MODL_CODE = '" + MODL_CODE + "'                    \n");
			sql.append("  WITH UR                                                      \n");

			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				CARS_CODE = rs.getString("CARS_CODE");
			}
		} catch(Exception e) {
			System.out.println("# sql : \n" + sql);
			e.printStackTrace();
		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
		}

		return CARS_CODE;
	}



	/**
	 * 차종코드로 모델코드 얻기
	 */
	public static String getModlCode(Connection con, String CORP_CODE, String CARS_CODE) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		String MODL_CODE = "";

		try {
			sql.append("SELECT CARS_CODE                                               \n");
			sql.append("      ,CARS_MODL_CODE                                          \n");
			sql.append("      ,CARS_MODL_NO                                            \n");
			sql.append("  FROM MCMFLE.LFRBCARS                                         \n");
			sql.append(" WHERE CARS_CORP_CODE = MCMFLE.FNRMCMIQ07('" + CORP_CODE + "') \n");
			sql.append("   AND CARS_CODE      = '" + CARS_CODE + "'                    \n");
			sql.append("  WITH UR                                                      \n");

			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				MODL_CODE = rs.getString("CARS_MODL_CODE");
			}
		} catch(Exception e) {
			System.out.println("# sql : \n" + sql);
			e.printStackTrace();
		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
		}

		return MODL_CODE;
	}


	/**
	 * 모델코드로 차종코드 얻기
	 */
	public static String getCarsCodeFromModlCode(Connection con, String CORP_CODE, String MODL_CODE) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		String CARS_CODE = "";

		try {
			sql.append("SELECT CARS_CODE                                               \n");
			sql.append("      ,CARS_MODL_CODE                                          \n");
			sql.append("      ,CARS_MODL_NO                                            \n");
			sql.append("  FROM MCMFLE.LFRBCARS                                         \n");
			sql.append(" WHERE CARS_CORP_CODE = MCMFLE.FNRMCMIQ07('" + CORP_CODE + "') \n");
			sql.append("   AND CARS_MODL_CODE = '" + MODL_CODE + "'                    \n");
			sql.append("  WITH UR                                                      \n");

			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				CARS_CODE = rs.getString("CARS_CODE");
			}
		} catch(Exception e) {
			System.out.println("# sql : \n" + sql);
			e.printStackTrace();
		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
		}

		return CARS_CODE;
	}



	/**
	 * 차종코드로 모델NO 얻기
	 */
	public static String getModlNo(Connection con, String CORP_CODE, String CARS_CODE) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		String MODL_NO = "";

		try {
			sql.append("SELECT CARS_CODE                                               \n");
			sql.append("      ,CARS_MODL_CODE                                          \n");
			sql.append("      ,CARS_MODL_NO                                            \n");
			sql.append("  FROM MCMFLE.LFRBCARS                                         \n");
			sql.append(" WHERE CARS_CORP_CODE = MCMFLE.FNRMCMIQ07('" + CORP_CODE + "') \n");
			sql.append("   AND CARS_CODE      = '" + CARS_CODE + "'                    \n");
			sql.append("  WITH UR                                                      \n");

			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				MODL_NO = rs.getString("CARS_MODL_NO");
			}
		} catch(Exception e) {
			System.out.println("# sql : \n" + sql);
			e.printStackTrace();
		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
		}

		return MODL_NO;
	}



	/**
	 * 모델코드로 모델NO 얻기
	 */
	public static String getModlNoFromModlCode(Connection con, String CORP_CODE, String MODL_CODE) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		String MODL_NO = "";

		try {
			sql.append("SELECT CARS_CODE                                               \n");
			sql.append("      ,CARS_MODL_CODE                                          \n");
			sql.append("      ,CARS_MODL_NO                                            \n");
			sql.append("  FROM MCMFLE.LFRBCARS                                         \n");
			sql.append(" WHERE CARS_CORP_CODE = MCMFLE.FNRMCMIQ07('" + CORP_CODE + "') \n");
			sql.append("   AND CARS_MODL_CODE = '" + MODL_CODE + "'                    \n");
			sql.append("  WITH UR                                                      \n");

			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				MODL_NO = rs.getString("CARS_MODL_NO");
			}
		} catch(Exception e) {
			System.out.println("# sql : \n" + sql);
			e.printStackTrace();
		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
		}

		return MODL_NO;
	}



	/**
	 * 원가산정 테이블 복사
	 * 원가산정에서만 사용
	 *
	 * 전제 조건 1 : fromKeys는 테이블의 맨 처음부터 사용(PK1, PK2, PK3...)
	 * 전제 조건 2 :   toKeys는 테이블의 맨 처음부터 사용(PK1, PK2, PK3...)
	 * 전제 조건 3 : 등록자, 수정자 컬럼은 "_INIT_CMAN", "_UPDT_CMAN"으로 끝나야 한다.(아닐 경우 원본값이 복사된다.)
	 * 전제 조건 4 : 등록일, 수정일 컬럼은 "_INIT_DATE", "_UPDT_DATE"으로 끝나야 한다.(아닐 경우 원본값이 복사된다.)
	 * 전제 조건 5 : 등록팀, 수정팀 컬럼은 "_INIT_TEAM", "_UPDT_TEAM"으로 끝나야 한다.(아닐 경우 원본값이 복사된다.)
	 * 전제 조건 6 : table명은 full로 줘야한다. 예) MCMFLE.PFRCCMMS
	 */
	public static int copyTable(Connection con, String tableFullName, String[] fromKeys, String[] toKeys, String INIT_DATE, String EMPL_NUMB) throws Exception {
		int insertCount = 0;
		String[] columnNames = null;
		Statement stmt = null;
		String INIT_TEAM = null;
		String title = null;
		String catalog = null;
		String tableName = null;
		StringBuffer sql = new StringBuffer();

		try {
			// ======================================================================
			// 입력값 검증
			// ======================================================================
			if (con           == null                                          ) title = "DB CONNECTION";
			if (fromKeys      == null       || fromKeys.length == 0            ) title = "FROM KEY";
			if (toKeys        == null       || toKeys.length   == 0            ) title = "FROM KEY";
			if (EMPL_NUMB     == null       || EMPL_NUMB.equals("")            ) title = "SABN";
			if (INIT_DATE     == null       || INIT_DATE.equals("")            ) title = "INIT DATE";
			if (tableFullName == null       || tableFullName.equals("")        ) title = "TABLE NAME";
			if (tableFullName.length() < 15 || tableFullName.indexOf(".") == -1) title = "TABLE NAME";

			if (title != null) {
				throw new Exception(title + "이(가) 유효하지 않습니다.");
			}

			// INIT_TEAM 코드를 구한다.
			INIT_TEAM = McamsUtil.getDEPT_CODE(con, EMPL_NUMB);
			if (INIT_DATE == null || INIT_DATE.equals("")) {
				throw new Exception("팀 코드를 구할 수 없습니다.");
			}

			// CATALOG 분리
			StringTokenizer st = new StringTokenizer(tableFullName, ".");
			if (st.hasMoreTokens()) {
				catalog = st.nextToken();
				tableName = st.nextToken();
			}

			// 컬럼들을 구한다.
			columnNames = DBConnMgr.getColumnNames(con, catalog, tableName);
			if (columnNames == null || columnNames.length == 0) {
				throw new Exception("컬럼 정보를 구할 수 없습니다.[" + tableFullName + "]");
			}

			sql.setLength(0);


			// ===========================================
			// INSERT 절
			// ===========================================
			sql.append("INSERT INTO " + tableFullName + "\n");

			// ===========================================
			// SELECT 절
			// ===========================================
			sql.append("SELECT \n");

			for (int i=0; i < columnNames.length; i++) {
				if (i == 0) {
					sql.append("       ");
				} else {
					sql.append("      ,");
				}

				// TO_KEY인 경우
				if (i < toKeys.length) {
					sql.append("'" + toKeys[i] + "'");
				} else {
					if        (columnNames[i].indexOf("_UPDT_DATE") != -1) {
						sql.append("'" + INIT_DATE + "'");
					} else if (columnNames[i].indexOf("_INIT_DATE") != -1) {
						sql.append("'" + INIT_DATE + "'");
					} else if (columnNames[i].indexOf("_UPDT_CMAN") != -1) {
						sql.append("'" + EMPL_NUMB + "'");
					} else if (columnNames[i].indexOf("_INIT_CMAN") != -1) {
						sql.append("'" + EMPL_NUMB + "'");
					} else if (columnNames[i].indexOf("_UPDT_TEAM") != -1) {
						sql.append("'" + INIT_TEAM + "'");
					} else if (columnNames[i].indexOf("_INIT_TEAM") != -1) {
						sql.append("'" + INIT_TEAM + "'");
					} else {
						sql.append(columnNames[i]);
					}
				}

				sql.append("\n");
			}

			// ===========================================
			// FROM 절
			// ===========================================
			sql.append("  FROM " + tableFullName + "\n");


			// ===========================================
			// WHERE 절
			// ===========================================
			sql.append(" WHERE 1 = 1               \n");

			for (int i=0; i < fromKeys.length; i++) {
				sql.append("   AND " + columnNames[i] + " = '" + fromKeys[i] + "' \n");
			}

			stmt = con.createStatement();
			insertCount = stmt.executeUpdate(sql.toString());

		} catch(SQLException e) {
			System.out.println("# 오류 SQL : \n" + sql);
			throw e;
		} finally {
			DBConnMgr.closeStatement(stmt);
		}

		return insertCount;
	}


	/**
	 * 원가산정 테이블 데이터 삭제
	 * 원가산정에서만 사용
	 *
	 * 전제 조건 1 : fromKeys는 테이블의 맨 처음부터 사용(PK1, PK2, PK3...)
	 * 전제 조건 2 : table명은 full로 줘야한다. 예) MCMFLE.PFRCCMMS
	 */
	public static int deleteData(Connection con, String tableFullName, String[] fromKeys) throws Exception {
		int deleteCount = 0;
		String[] columnNames = null;
		Statement stmt = null;
		String title = null;
		String catalog = null;
		String tableName = null;
		StringBuffer sql = new StringBuffer();

		try {

			// ======================================================================
			// 입력값 검증
			// ======================================================================
			if (con           == null                                          ) title = "DB CONNECTION";
			if (fromKeys      == null       || fromKeys.length == 0            ) title = "FROM KEY";
			if (tableFullName == null       || tableFullName.equals("")        ) title = "TABLE NAME";
			if (tableFullName.length() < 15 || tableFullName.indexOf(".") == -1) title = "TABLE NAME";

			if (title != null) {
				throw new Exception(title + "이(가) 유효하지 않습니다.");
			}

			// CATALOG 분리
			StringTokenizer st = new StringTokenizer(tableFullName, ".");
			if (st.hasMoreTokens()) {
				catalog = st.nextToken();
				tableName = st.nextToken();
			}

			// 컬럼들을 구한다.
			columnNames = DBConnMgr.getColumnNames(con, catalog, tableName);
			if (columnNames == null || columnNames.length == 0) {
				throw new Exception("컬럼 정보를 구할 수 없습니다.[" + tableFullName + "]");
			}

			// ===========================================
			// DELETE FROM 절
			// ===========================================
			sql.setLength(0);
			sql.append("DELETE FROM " + tableFullName + "\n");

			// ===========================================
			// WHERE 절
			// ===========================================
			sql.append(" WHERE 1 = 1               \n");

			for (int i=0; i < fromKeys.length; i++) {
				sql.append("   AND " + columnNames[i] + " = '" + fromKeys[i] + "' \n");
			}

			////System.out.println("# 일괄삭제 SQL : \n" + sql);

			stmt = con.createStatement();
			deleteCount = stmt.executeUpdate(sql.toString());
		} catch(SQLException e) {
			System.out.println("# 오류 SQL : \n" + sql);
			throw e;
		} finally {
			DBConnMgr.closeStatement(stmt);
		}

		return deleteCount;
	}


	/**
	 * 로그인자의 결재 순번 얻기
	 */
	public static int getApprNumber(Connection con, String CORP_CODE, int APPR_NO, String COMH_SABN) throws Exception {

		ResultSet rs = null;
		Statement stmt = null;
		StringBuffer sql = new StringBuffer();
		String COMD_APPR_CMAN = null;
		int APPR_NUMB = 0;
		try {
			sql.append("   SELECT APPR_APR1_CMAN      COMD_APPR_CMAN1                 \n");
			sql.append("         ,APPR_APR2_CMAN      COMD_APPR_CMAN2                 \n");
			sql.append("         ,APPR_APR3_CMAN      COMD_APPR_CMAN3                 \n");
			sql.append("         ,APPR_APR4_CMAN      COMD_APPR_CMAN4                 \n");
			sql.append("         ,APPR_APR5_CMAN      COMD_APPR_CMAN5                 \n");
			sql.append("         ,APPR_APR6_CMAN      COMD_APPR_CMAN6                 \n");
			sql.append("         ,APPR_APR7_CMAN      COMD_APPR_CMAN7                 \n");
			sql.append("     FROM MCMFLE.PFRCAPPR                                     \n");
			sql.append("    WHERE APPR_CORP_CODE = '" + CORP_CODE + "'                \n");
			sql.append("      AND APPR_NO = '" + APPR_NO + "'                         \n");
			sql.append("     WITH UR                                                  \n");

			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				for (int i=1; i<8; i++) {
					COMD_APPR_CMAN = rs.getString(i);
					if (COMH_SABN.equals(COMD_APPR_CMAN) == true) {
						APPR_NUMB = i;
						break;
					}
				}
			}


		} catch(Exception e) {
			System.out.println("# sql : \n" + sql);
			e.printStackTrace();
		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
		}

		return APPR_NUMB;
	}


	/**
	 * 로그인자가 최종 결재자 여부 얻기
	 */
	public static boolean getLastAppr(Connection con, String CORP_CODE, int APPR_NO, String COMH_SABN) throws Exception {

		ResultSet rs = null;
		Statement stmt = null;
		StringBuffer sql = new StringBuffer();
		String COMD_APPR_CMAN = null;

		try {
			sql.append("   SELECT APPR_APR1_CMAN      COMD_APPR_CMAN1                 \n");
			sql.append("         ,APPR_APR2_CMAN      COMD_APPR_CMAN2                 \n");
			sql.append("         ,APPR_APR3_CMAN      COMD_APPR_CMAN3                 \n");
			sql.append("         ,APPR_APR4_CMAN      COMD_APPR_CMAN4                 \n");
			sql.append("         ,APPR_APR5_CMAN      COMD_APPR_CMAN5                 \n");
			sql.append("         ,APPR_APR6_CMAN      COMD_APPR_CMAN6                 \n");
			sql.append("         ,APPR_APR7_CMAN      COMD_APPR_CMAN7                 \n");
			sql.append("     FROM MCMFLE.PFRCAPPR                                     \n");
			sql.append("    WHERE APPR_CORP_CODE = '" + CORP_CODE + "'                \n");
			sql.append("      AND APPR_NO = '" + APPR_NO + "'                         \n");
			sql.append("     WITH UR                                                  \n");

			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {

				for (int i=1; i<8; i++) {
					COMD_APPR_CMAN = rs.getString(i);
					if (COMH_SABN.equals(COMD_APPR_CMAN) == true) {
						if (rs.getString(i+1).equals("") == true) {
							break;
						} else {
							return false;
						}
					}
					if (i == 7) {
						throw new Exception("결재정보와 일치하는 사번이 없습니다. [MCMFLE.PFRCAPPR not SELECT]");
					}
				}
			}

			/*
    	} catch(Exception e) {
    		System.out.println("# sql : \n" + sql);
    		e.printStackTrace();
			 */
		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
		}

		return true;
	}


	/**
	 * 특정 업무(화면)에 대해 권한이 있는가 여부
	 */
	public static boolean isSpecialUser(String PRGM_SPID, String EMPL_NUMB) throws Exception {
		Connection con = null;
		ResultSet rs = null;
		Statement stmt = null;
		StringBuffer sql = new StringBuffer();
		boolean authorized = false;

		try {
			sql.append("SELECT CODE_LANG_CODE EMPL_NAME              \n");
			sql.append("  FROM MCMFLE.PFRMCODE                       \n");
			sql.append(" WHERE CODE_GUBN_CODE = '" + PRGM_SPID + "'  \n");
			sql.append("   AND CODE_CODE      = '" + EMPL_NUMB + "'  \n");
			sql.append("  WITH UR                                    \n");

			con = DBConnMgr.getMcamsConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				authorized = true;
			}

		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
			DBConnMgr.closeConnection(con);
		}

		return authorized;
	}


	/**
	 * 실행 모드를 알아내어 개발인 경우 SP의 맨 끝에 "_T"를 추가해서 리턴한다.
	 */
	public static String getSP(String SP) throws Exception {
		if (McamsConstant.isDev()) {
			SP = SP + "_T";
		}

		return SP;
	}


	/**
	 * 스트링으로 받은 소숫점 3자리 이상의 원가데이터를 소숫점 2이하로 절삭한다. (substring)
	 */
	public static String getTruncPercent(String SP) throws Exception {

		int lastIndex = SP.indexOf(".");
		if (lastIndex != -1) {
			lastIndex = lastIndex + 3;
			SP = SP.substring(0,lastIndex);
		}
		return SP;
	}


	/**
	 * 스트링으로 받은 원가데이터의 소숫점 이하를 절삭한다. (substring)
	 */
	public static String getTruncStr(String SP) throws Exception {

		int lastIndex = SP.indexOf(".");
		if (lastIndex != -1) {
			SP = SP.substring(0,lastIndex);
		}
		return SP;
	}


	/**
	 * 외부SEQ로 내부SEQ얻기 : END PART
	 */
	public static int getCUMT_NO(String CORP_CODE, String COST_GUBN, String PART_NO, String EONO, String EXTR_SEQ0) throws Exception {
		System.out.println("##### getCUMT_NO #####################");
		return getCUMT_NO(DBConnMgr.getMcamsConnection(), CORP_CODE, COST_GUBN, PART_NO, EONO, EXTR_SEQ0);
	}


	/**
	 * 외부SEQ로 내부SEQ얻기 : END PART
	 */
	public static int getCUMT_NO(Connection con, String CORP_CODE, String COST_GUBN, String PART_NO, String EONO, String EXTR_SEQ0) throws Exception {
		System.out.println("# NOT FOUND SQL : \n");
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		int CUMT_NO = -1;

		try {
			sql.append("SELECT EMST_CUMT_NO                         \n");
			sql.append("  FROM MCMFLE.PFRCEMST                      \n");
			sql.append(" WHERE EMST_CORP_CODE = '" + CORP_CODE + "' \n");
			sql.append("   AND EMST_COST_GUBN = '" + COST_GUBN + "' \n");
			sql.append("   AND EMST_PART_NO   = '" + PART_NO   + "' \n");
			sql.append("   AND EMST_EONO      = '" + EONO      + "' \n");
			sql.append("   AND EMST_EXTR_SEQ0 =  " + EXTR_SEQ0 + "  \n");
			sql.append("  WITH UR                                   \n");

			System.out.println(sql);

			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				CUMT_NO = rs.getInt("EMST_CUMT_NO");
			} else {
				System.out.println("# NOT FOUND SQL : \n" + sql);
			}
		} catch(Exception e) {
			System.out.println("# 오류 SQL : \n" + sql);
			throw e;
		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
		}

		return CUMT_NO;
	}



	/**
	 * 내부SEQ로 외부SEQ얻기 : END PART
	 */
	public static int getEXTR_SEQ0(Connection con, String CORP_CODE, String COST_GUBN, String PART_NO, String EONO, String CUMT_NO) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		int EXTR_SEQ0 = 0;

		try {
			sql.append("SELECT EMST_EXTR_SEQ0                       \n");
			sql.append("  FROM MCMFLE.PFRCEMST                      \n");
			sql.append(" WHERE EMST_CORP_CODE = '" + CORP_CODE + "' \n");
			sql.append("   AND EMST_COST_GUBN = '" + COST_GUBN + "' \n");
			sql.append("   AND EMST_PART_NO   = '" + PART_NO   + "' \n");
			sql.append("   AND EMST_EONO      = '" + EONO      + "' \n");
			sql.append("   AND EMST_CUMT_NO   =  " + CUMT_NO   + "  \n");
			sql.append("  WITH UR                                   \n");

			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				EXTR_SEQ0 = rs.getInt("EMST_EXTR_SEQ0");
			} else {
				System.out.println("# NOT FOUND SQL : \n" + sql);
			}
		} catch(Exception e) {
			System.out.println("# 오류 SQL : \n" + sql);
			throw e;
		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
		}

		return EXTR_SEQ0;
	}



	/**
	 * 내부SEQ로 외부SEQ얻기 : SUB PART
	 */
	public static int getEXTR_SEQ0(Connection con, String CORP_CODE, String PART_NO, String EONO, String CUMT_NO) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		int EXTR_SEQ0 = 0;

		try {
			sql.append("SELECT SPMS_EXTR_SEQ0                       \n");
			sql.append("  FROM MCMFLE.PFRCSPMS                      \n");
			sql.append(" WHERE SPMS_CORP_CODE = '" + CORP_CODE + "' \n");
			sql.append("   AND SPMS_PART_NO   = '" + PART_NO   + "' \n");
			sql.append("   AND SPMS_EONO      = '" + EONO      + "' \n");
			sql.append("   AND SPMS_CUMT_NO   =  " + CUMT_NO   + "  \n");
			sql.append("  WITH UR                                   \n");

			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				EXTR_SEQ0 = rs.getInt("SPMS_EXTR_SEQ0");
			} else {
				System.out.println("# NOT FOUND SQL : \n" + sql);
			}
		} catch(Exception e) {
			System.out.println("# 오류 SQL : \n" + sql);
			throw e;
		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
		}

		return EXTR_SEQ0;
	}




	/**
	 * request uri를 이용하여 PRGM ID를 구한다.
	 */
	public static String getPRGM_ID(HttpServletRequest req) throws Exception {
		int beginIndex = 0;
		String PRGM_ID = "";
		String uri = req.getRequestURI();

		// Servlet의 경우
		if (uri.indexOf("/servlet/") != -1) {
			// "/mcams/servlet/mobis.mcams.pc.servlet.JAR_PCB0_S"
			beginIndex = uri.indexOf("JAR_") + 4;
			// Jsp의 경우
		} else if (uri.indexOf(".jsp") != -1) {
			// "/mcams/pm/JVR_PM10_V.jsp"
			beginIndex = uri.indexOf("JVR_") + 4;
		}

		PRGM_ID = uri.substring(beginIndex, beginIndex + 4);

		return PRGM_ID;
	}


	/**
	 * PRGM ID를 이용하여 PRGM NAME을 구한다.
	 */
	public static String getPRGM_NAME(ServletContext application, String PRGM_ID) throws Exception {
		String PRGM_NAME = "";
		Map PRGM_MAP = null;

		if (PRGM_ID == null) {
			PRGM_ID = "";
		}

		try {
			PRGM_MAP = (HashMap)application.getAttribute(MCAMS_PGRM_INFO);
		} catch(ClassCastException e) {
			// 아직 설정이 안 된 경우다.
		}

		if (PRGM_MAP == null) {
			PRGM_MAP = getPRGM_INFO();
			application.setAttribute(MCAMS_PGRM_INFO, PRGM_MAP);
		}

		PRGM_NAME = (String)PRGM_MAP.get(PRGM_ID);

		return PRGM_NAME;
	}



	/**
	 * PRGM ID, PRGM NAME을 구해서 map으로 리턴한다.
	 */
	public static Map getPRGM_INFO() throws Exception {
		Connection con = null;
		Statement  stmt = null;
		ResultSet  rs   = null;
		StringBuffer sql = new StringBuffer();
		Map map = new HashMap();

		try {
			//con = DBConnMgr.getMcamsConnection();
			con = Database.getConnection();
			stmt = con.createStatement();

			sql.append("SELECT PRGM_PRGM_CODE    \n");
			sql.append("      ,PRGM_NAME_KORN    \n");
			sql.append("  FROM MCMFLE.LFRMPRGM   \n");
			sql.append(" ORDER BY PRGM_PRGM_CODE \n");
			sql.append("  WITH UR                \n");

			rs = stmt.executeQuery(sql.toString());

			while (rs.next()) {
				map.put(rs.getString("PRGM_PRGM_CODE"), rs.getString("PRGM_NAME_KORN"));
			}
		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
			//DBConnMgr.closeConnection(con);
			if(con != null) try { con.close(); } catch (SQLException e) {}
		}

		return map;
	}


	/**
	 * 승인난 SUB-PART의 최신 CUMT_NO 얻기
	 *
	 * - 동일 원가계산구분코드
	 * - 동일 부문
	 * - 동일 PART NO
	 * - 동일 EONO
	 * - 승인난 것
	 * - AS/영업 대응이 아닌것
	 * - MPIMS적용이 아닌것
	 */
	public static int getLAST_CUMT_NO_SUB0(String CORP_CODE, String PART_NO, String EONO, String COST_GUBN, String BUMN_GUBN) throws Exception {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		int CUMT_NO = 0;

		if (BUMN_GUBN == null || BUMN_GUBN.equals("")) {
			throw new Exception("부문코드가 유효하지 않습니다.");
		}

		try {
			sql.append("SELECT MCMFLE.FNRCD0IQ03    \n");
			sql.append("(                           \n");
			sql.append("  '" + CORP_CODE + "'       \n");
			sql.append(" ,'" + PART_NO   + "'       \n");
			sql.append(" ,'" + EONO      + "'       \n");
			sql.append(" ,'" + COST_GUBN + "'       \n");
			sql.append(" ,'" + BUMN_GUBN + "'       \n");
			sql.append(" )                          \n");
			sql.append("FROM SYSIBM.SYSDUMMY1       \n");

			con = DBConnMgr.getMcamsConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				CUMT_NO = rs.getInt(1);
			}
		} catch(Exception e) {
			System.out.println("# 오류 SQL : \n" + sql);
			throw e;
		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
			DBConnMgr.closeConnection(con);
		}

		return CUMT_NO;
	}



	/**
	 * 사용법인코드로 대표법인코드 얻기
	 * 2011.06.22 HJJ : 인도, 북미 대표법인 추가
	 * 2011.12.22 HJJ : 중국법인 변경 (CNS1 --> 2400, CNS2 --> 2600, CNS3 --> 2500, CNS4 --> 사용안됨
	 */
	public static String getCORP_ALL0(String CORP_CODE) throws Exception {
		String CORP_ALL0 = "";

		if (CORP_CODE == null || CORP_CODE.equals("")) {
			throw new BusinessException("NOT VALID CORP_CODE CODE!!!<" + CORP_CODE + ">");
		}

		if (
				CORP_CODE.equals("CNB1") ||
				CORP_CODE.equals("CNB2") ||
				CORP_CODE.equals("CNB3") ||
				CORP_CODE.equals("2100") ||
				CORP_CODE.equals("2300") ||
				CORP_CODE.equals("2400") ||
				CORP_CODE.equals("2600") ||
				CORP_CODE.equals("2500") ||
				CORP_CODE.equals("2900") ||
				CORP_CODE.equals("C100") ||
				CORP_CODE.equals("C300") ||
				CORP_CODE.equals("C400") ||
				CORP_CODE.equals("C600") ||
				CORP_CODE.equals("C500") ||
				CORP_CODE.equals("C900") ||
				CORP_CODE.equals("2A00") || //2016.09.29 창주 법인 ADD
				CORP_CODE.equals("2C00")  //2017.08.22 충칭 법인 ADD
				) {
			CORP_ALL0 = "CNAL";

		} else if (CORP_CODE.equals("4200") || CORP_CODE.equals("4700")) {
			CORP_ALL0 = "INAL";

		} else if (
				CORP_CODE.equals("5100") ||
				CORP_CODE.equals("5600") ) {
			CORP_ALL0 = "USAL";

		// 나머지는 각자 작기 법인 코드를 그대로 사용함.
		} else {
			CORP_ALL0 = CORP_CODE;
		}

		return CORP_ALL0;
	}

	/**
	 * 중국 법인 코드 각각 6개로 나누어서 사용 
	 */
	public static String getCORP_CODE(String CORP_CODE) throws Exception {
		String CORP_ALL0 = "";

		if (CORP_CODE == null || CORP_CODE.equals("")) {
			throw new BusinessException("NOT VALID CORP_CODE CODE!!![" + CORP_CODE + "]");
		}

		if (CORP_CODE.equals("4200") || CORP_CODE.equals("4700")) {
			CORP_ALL0 = "INAL";
		} else if (
				CORP_CODE.equals("5100") ||
				CORP_CODE.equals("5600") ) {
			CORP_ALL0 = "USAL";
			// 나머지는 각자 작기 법인 코드를 그대로 사용함.
		} else {
			CORP_ALL0 = CORP_CODE;
		}

		return CORP_ALL0;
	}

	/**
	 * 법인에 따른 통화 코드(알파벳 3자) 얻기(KRW/RMB/INR/USD)
	 * 2011.06.22 HJJ : 인도, 북미 추가
	 * 2011.12.22 HJJ : 중국법인 변경 (CNS1 --> 2400, CNS2 --> 2600, CNS3 --> 2500, CNS4 --> 사용안됨
	 */
	public static String getCurrencyFromCorp(String CORP_CODE) throws Exception {
		String CURR_MARK = "";

		// 본사
		if (CORP_CODE.equals("1000") || CORP_CODE.equals("9900") || CORP_CODE.equals("1400") || CORP_CODE.equals("1300")) {
			CURR_MARK = "KRW";
			// 중국
		} else if (StringUtils.in(CORP_CODE, "CNAL", "2100", "2300", "2400", "2500", "2600", "2900", "2A00")) {  //2016.09.29 창주 법인 ADD
			CURR_MARK = "CNY";
			// 중국
		} else if (StringUtils.in(CORP_CODE, "CNB1", "CNB2", "CNB3", "C100", "C300", "C400", "C500", "C600", "C900")) {
			CURR_MARK = "CNY";
			// 인도
		} else if (StringUtils.in(CORP_CODE, "INAL", "4200", "4700")) {
			CURR_MARK = "INR";
			// 미국-앨라바마, 미국-조지아
		} else if (StringUtils.in(CORP_CODE, "USAL", "5100", "5600")) {
			CURR_MARK = "USD";
			// 체코 / 슬로박
		} else if (StringUtils.in(CORP_CODE, "6300", "6500")) {
			CURR_MARK = "EUR";
			// 브라질
		} else if (StringUtils.in(CORP_CODE, "5700")) {
			CURR_MARK = "BRL";
			// 러시아
		} else if (StringUtils.in(CORP_CODE, "6600")) {
			CURR_MARK = "RUB";
			// 멕시코
		} else if (StringUtils.in(CORP_CODE, "5B00")) {
			CURR_MARK = "MXN";
		} else {
			throw new BusinessException("NOT VALID CORP_CODE CODE!!![" + CORP_CODE + "]");
		}

		return CURR_MARK;
	}



	/**
	 * 통화 기호 얻기(특수문자 1자)
	 * 2011.06.22 HJJ : 인도 기호 추가
	 */
	public static String getCurrencyMark(String CURR_CODE) {
		String CURR_MARK = "";

		// 본사
		if (CURR_CODE.equals("KRW") || CURR_CODE.equals("KSW")) {
			CURR_MARK = "￦";
			// 미국
		} else if (CURR_CODE.equals("USD")) {
			CURR_MARK = "$";
			// 일본
		} else if (CURR_CODE.equals("JPY")) {
			CURR_MARK = "￥";
			// 중국1
		} else if (CURR_CODE.equals("RMB")) {
			CURR_MARK = "RMB";
			// 중국2
		} else if (CURR_CODE.equals("CNY")) {
			CURR_MARK = "元";
			// 영국
		} else if (CURR_CODE.equals("GBP")) {
			CURR_MARK = "￡";
			// 인도
		} else if (CURR_CODE.equals("INR")) {
			CURR_MARK = "Rs";
			// 체코/슬로바키아
		} else if (CURR_CODE.equals("EUR")) {
			CURR_MARK = "€";
			// 브라질
		} else if (CURR_CODE.equals("BRL")) {
			CURR_MARK = "R$";
			// 러시아
		} else if (CURR_CODE.equals("RUB")) {
			CURR_MARK = "руб";
		} else if (CURR_CODE.equals("")) {
			CURR_MARK = "";
		} else {
			System.out.println("# CURR_MARK = N/A, CURR_CODE : " + CURR_CODE);
			CURR_MARK = "N/A";
		}

		return CURR_MARK;
	}


	/**
	 * 통화 기호(특수문자 1자) 얻기
	 * 2011.06.22 HJJ : 인도, 북미 기호 추가
	 * 2011.12.22 HJJ : 중국법인 변경 (CNS1 --> 2400, CNS2 --> 2600, CNS3 --> 2500, CNS4 --> 사용안됨
	 */
	public static String getCurrMarkFromCorp(String CORP_CODE) throws Exception {
		String CURR_MARK = "";

		// 본사
		if (CORP_CODE.equals("1000") || CORP_CODE.equals("9900") || (CORP_CODE.equals("1400") || (CORP_CODE.equals("1300")))) {
			CURR_MARK = "￦";
			// 중국
		} else if(
				CORP_CODE.equals("CNB1") ||
				CORP_CODE.equals("CNB2") ||
				CORP_CODE.equals("CNB3") ||
				CORP_CODE.equals("2100") ||
				CORP_CODE.equals("2300") ||
				CORP_CODE.equals("2400") ||
				CORP_CODE.equals("2600") ||
				CORP_CODE.equals("2500") ||
				CORP_CODE.equals("2900") ||
				CORP_CODE.equals("C100") ||
				CORP_CODE.equals("C300") ||
				CORP_CODE.equals("C400") ||
				CORP_CODE.equals("C600") ||
				CORP_CODE.equals("C500") ||
				CORP_CODE.equals("C900") ||
				CORP_CODE.equals("2A00") //2016.09.29 창주 법인 ADD
				) {
			CURR_MARK = "RMB";
			// 인도
		} else if(CORP_CODE.equals("4200") || CORP_CODE.equals("4700")) {
			CURR_MARK = "INR";
			// 미국
		} else if(CORP_CODE.equals("5100") || CORP_CODE.equals("5600")) {
			CURR_MARK = "$";
			// 체코/슬로바키아
		} else if(CORP_CODE.equals("6500") || CORP_CODE.equals("6300")) {
			CURR_MARK = "EUR";
			// 브라질
		} else if(CORP_CODE.equals("5700")) {
			CURR_MARK = "BRL";
			// 러시아
		} else if(CORP_CODE.equals("6600")) {
			CURR_MARK = "RUB";
			// 멕시코
		} else if(CORP_CODE.equals("5B00")) {
			CURR_MARK = "MXN";			
		} else {
			throw new BusinessException("NOT VALID CORP_CODE CODE!!![" + CORP_CODE + "]");
		}

		return CURR_MARK;
	}



	/**
	 * 테이블 데이터 조회
	 * : 데이터 1건만 조회
	 */
	public static DataSet select(String tableName, String key, String value) throws Exception {
		return select(tableName, new String[] {key}, new String[] {value});
	}


	/**
	 * 테이블 데이터 조회
	 * : 데이터 1건만 조회
	 */
	public static DataSet select(Connection con, String tableName, String key, String value) throws Exception {
		return select(con, tableName, new String[] {key}, new String[] {value});
	}




	/**
	 * 테이블 데이터 조회
	 * : 데이터 1건만 조회
	 */
	public static DataSet select(String tableName, String[] keys, String[] values) throws Exception {
		Connection con = DBConnMgr.getMcamsConnection();

		return select(con, tableName, keys, values);
	}




	/**
	 * 테이블 데이터 조회
	 * : 데이터 1건만 조회
	 */
	public static DataSet select(Connection con, String tableName, String[] keys, String[] values) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		DataSet ds = new DataSet();

		try {
			sql.append("SELECT *                           \n");
			sql.append("  FROM " + tableName + "           \n");
			sql.append(" WHERE 1 = 1                       \n");

			for (int i=0; i < keys.length; i++) {
				sql.append("   AND " + keys[i] + " = '" + values[i] + "' \n");
			}

			sql.append(" FETCH FIRST 1 ROWS ONLY           \n");
			sql.append("  WITH UR                          \n");


			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				String columnName = null;
				ResultSetMetaData rsmd = rs.getMetaData();

				for (int i=1; i <= rsmd.getColumnCount(); i++) {
					columnName = rsmd.getColumnName(i);
					ds.set(columnName, rs.getString(columnName));
				}
			}
		} catch(Exception e) {
			System.out.println("# ERROR SQL : \n" + sql);
			throw e;
		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
		}

		return ds;
	}



	/**
	 * 테이블 데이터 조회
	 * : 컬럼 지정
	 */
	public static DataSet select(String tableName, String[] cols, String[] keys, String[] values) throws Exception {
		Connection con = DBConnMgr.getMcamsConnection();

		return select(con, tableName, cols, keys, values);
	}



	/**
	 * 테이블 데이터 조회
	 * : 컬럼 지정
	 */
	public static DataSet select(Connection con, String tableName, String[] cols, String[] keys, String[] values) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		DataSet ds = new DataSet();

		try {
			sql.append("SELECT -1 DUMY                     \n");

			for (int i=0; i < cols.length; i++) {
				sql.append("   ," + cols[i] + "            \n");
			}

			sql.append("  FROM " + tableName + "           \n");
			sql.append(" WHERE 1 = 1                       \n");

			for (int i=0; i < keys.length; i++) {
				sql.append("   AND " + keys[i] + " = '" + values[i] + "' \n");
			}

			sql.append("  WITH UR                          \n");

			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				String columnName = null;
				ResultSetMetaData rsmd = rs.getMetaData();

				for (int i=1; i <= rsmd.getColumnCount(); i++) {
					columnName = rsmd.getColumnName(i);
					ds.set(columnName, rs.getString(columnName));
				}
			}
		} catch(Exception e) {
			System.out.println("# 오류 SQL : \n" + sql);
			throw e;
		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
		}

		return ds;
	}


	/**
	 * 테이블 데이터 수정
	 */
	public static int update(Connection con, String tableName, String[] columns, String[] colValues, String[] keys, String[] values) throws Exception {
		Statement stmt = null;
		StringBuffer sql = new StringBuffer();
		int updateCount = 0;

		try {
			sql.append("UPDATE " + tableName + "                              \n");
			sql.append("   SET                                                \n");

			for (int i=0; i < columns.length; i++) {
				if (i > 0) {
					sql.append(",");
				}

				if (colValues[i].equals("NULL")) {
					sql.append("      " + columns[i] + " =  " + colValues[i] + "  \n");
				} else {
					sql.append("      " + columns[i] + " = '" + colValues[i] + "' \n");
				}
			}

			sql.append(" WHERE 1 = 1                                          \n");

			for (int i=0; i < keys.length; i++) {
				sql.append("   AND " + keys[i] + " = '" + values[i] + "'      \n");
			}

			stmt = con.createStatement();
			updateCount = stmt.executeUpdate(sql.toString());
		} catch(Exception e) {
			System.out.println("# 오류 SQL : \n" + sql);
			throw e;
		} finally {
			DBConnMgr.closeStatement(stmt);
		}

		return updateCount;
	}


	/**
	 * 테이블 데이터 삭제
	 */
	public static int delete(Connection con, String tableName, String[] keys, String[] values) throws Exception {
		Statement stmt = null;
		StringBuffer sql = new StringBuffer();
		int deleteCount = 0;

		try {
			sql.append("DELETE                             \n");
			sql.append("  FROM " + tableName + "           \n");
			sql.append(" WHERE 1 = 1                       \n");

			for (int i=0; i < keys.length; i++) {
				if (values[i].equals("NULL") || values[i].equals("null")) {
					sql.append("   AND " + keys[i] + " IS NULL \n");
				} else {
					sql.append("   AND " + keys[i] + " = '" + values[i] + "' \n");
				}
			}

			stmt = con.createStatement();
			deleteCount = stmt.executeUpdate(sql.toString());

			System.out.println("# McamsUtil.delete().TABLE[" + tableName + "] DELETE COUNT : " + deleteCount);
		} catch(Exception e) {
			System.out.println("# ERROR SQL : \n" + sql);
			throw e;
		} finally {
			DBConnMgr.closeStatement(stmt);
		}

		return deleteCount;
	}



	/**
	 * 데이터가 존재하는가?
	 */
	public static boolean exist(Connection con, String tableName, String[] keys, String[] values) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		boolean data_exist = false;

		try {
			sql.append("SELECT 'FOUND'                             \n");
			sql.append("  FROM SYSIBM.SYSDUMMY1                    \n");
			sql.append(" WHERE EXISTS (                            \n");
			sql.append("               SELECT 'FOUND'              \n");
			sql.append("                 FROM " + tableName + "    \n");
			sql.append("                WHERE 1 = 1                \n");

			for (int i=0; i < keys.length; i++) {
				if (values[i].equals("NULL") || values[i].equals("null")) {
					sql.append("   AND " + keys[i] + " IS NULL \n");
				} else {
					sql.append("   AND " + keys[i] + " = '" + values[i] + "' \n");
				}
			}

			sql.append("              )                            \n");
			sql.append(" WITH UR                                   \n");

			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				data_exist = true;
			} else {
				data_exist = false;
			}
		} catch(Exception e) {
			System.out.println("# ERROR SQL : \n" + sql);
			throw e;
		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
		}

		return data_exist;
	}




	/**
	 * 공법 정보를 구해서 DataSet으로 리턴한다.
	 */
	public DataSet getCOME_INFO(HttpServletRequest req) throws Exception, BusinessException {
		Connection con = null;
		Statement  stmt = null;
		ResultSet  rs   = null;
		StringBuffer sql = new StringBuffer();
		String TODAY = DateUtils.getToday();
		String LANG = McamsLoginHelper.getLANG_CODE(req);
		String CORP_ALL0 = McamsLoginHelper.getCORP_ALL0(req);
		String EMPL_NUMB = McamsLoginHelper.getEMPL_NUMB(req);
		DataSet ds_come = new DataSet();

		try {
			con = DBConnMgr.getMcamsConnection();
			stmt = con.createStatement();

			sql.append("SELECT ROW_NUMBER() OVER(                                                    \n");
			sql.append("         ORDER BY COME_SORT_KEY1                                             \n");
			sql.append("                 ,COME_NAME                                                  \n");
			sql.append("       ) COME_SORT_KEY0                                                      \n");
			sql.append("      ,COME_CODE                                                             \n");
			sql.append("      ,COME_NAME                                                             \n");
			sql.append("      ,COME_INDS_CODE                                                        \n");
			sql.append("      ,CMDG_DEGR                                                             \n");
			sql.append("      ,CMDG_DEGR_HKMC                                                        \n");
			sql.append("      ,COME_CURR_APPL                                                        \n");
			sql.append("  FROM                                                                       \n");
			sql.append("      (                                                                      \n");
			sql.append("       SELECT CASE                                                           \n");
			sql.append("                WHEN COME_CODE = 'MANU' THEN 1                               \n");
			sql.append("                WHEN COME_SORT_KEY0 BETWEEN 100 AND 200 THEN COME_SORT_KEY0  \n");
			sql.append("                ELSE 2                                                       \n");
			sql.append("              END COME_SORT_KEY1                                             \n");
			sql.append("             ,COME_CODE                                                      \n");
			sql.append("             ,MCMFLE.FNRMCMIQ02(COME_NAME_LNCD,'" + LANG + "') COME_NAME     \n");
			sql.append("             ,COME_SORT_KEY0                                                 \n");
			sql.append("             ,COME_INDS_CODE                                                 \n");
			sql.append("             ,CMDG_DEGR                                                      \n");
			sql.append("             ,CMDG_DEGR_HKMC                                                 \n");
			sql.append("             ,CASE                                                           \n");
			sql.append("                WHEN CMDG_APPL_FRDT <= '" + TODAY + "' THEN 'Y'              \n");
			sql.append("                ELSE                                   'N'                   \n");
			sql.append("              END COME_CURR_APPL                                             \n");
			sql.append("         FROM MCMFLE.PFRCCOME COME                                           \n");
			sql.append("                INNER JOIN                                                   \n");
			sql.append("              MCMFLE.PFRCCMDG CMDG                                           \n");
			sql.append("                  ON CMDG_CORP_CODE = COME_CORP_CODE                         \n");
			sql.append("                 AND CMDG_CODE = COME_CODE                                   \n");
			sql.append("                 AND CMDG_DEGR = COME_APPL_DEGR                              \n");
			sql.append("        WHERE COME_CORP_CODE = '" + CORP_ALL0 + "'                           \n");

			//if(CORP_ALL0.equals("CNAL") && !EMPL_NUMB.equals("ENGNU62")) {
			//	sql.append("          AND COME_CODE IN ('MANU', 'CPIJ' ,'CEPJ' ,'CPRE' ,'CMPR' ,'CPCT' ,'CSMD' ,'CWEL' ,'CSCT' ,'CSYM') \n");
			//}

			sql.append(" AND COME_CODE NOT IN ('HFSP','HPSP')     ) A                                                                    \n");
			sql.append(" ORDER BY COME_SORT_KEY0                                                     \n");
			sql.append("  WITH UR                                                                    \n");

			rs = stmt.executeQuery(sql.toString());

			while (rs.next()) {
				ds_come.add("COMD_SORT_KEY0", rs.getString("COME_SORT_KEY0"));
				ds_come.add("COMD_COME_CODE", rs.getString("COME_CODE"     ));
				ds_come.add("COMD_COME_NAME", rs.getString("COME_NAME"     ));
				ds_come.add("COMD_INDS_CODE", rs.getString("COME_INDS_CODE"));
				ds_come.add("COMD_DEGR_MBIS", rs.getString("CMDG_DEGR"     ));
				ds_come.add("COMD_DEGR_HKMC", rs.getString("CMDG_DEGR_HKMC"));
				ds_come.add("COMD_CURR_APPL", rs.getString("COME_CURR_APPL"));
			}

			if (ds_come.size("COMD_COME_CODE") == 0) {
				throw new BusinessException("공법 정보를 얻을 수 없습니다.");
			}
		} catch(BusinessException e) {
			System.out.println("# ERROR!!! SQL : \n" + sql.toString());
			throw e;
		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
			DBConnMgr.closeConnection(con);
		}

		return ds_come;
	}


	/**
	 * 테이블 데이터 삭제
	 */
	public static void deleteSessionTimeOutData() throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int deleteCount = 0;
		String date = null;

		try {
			//con = DBConnMgr.getMcamsConnection();
			con = Database.getConnection();

			date = DateUtils.formatDate(DateUtils.computeDay(-1) + DateUtils.getTime(), DateUtils.YYYYMMDDHHMISS);
			sql = "SELECT COUNT(*) CNT FROM MCMFLE.PFRMDBSS WHERE DBSS_LOGN_TIME < ? ";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, date);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				deleteCount = rs.getInt(1);
			}
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(pstmt);

			if (deleteCount > 0) {
				sql = "DELETE FROM MCMFLE.PFRMDBSS WHERE DBSS_LOGN_TIME < ? ";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, date);
				pstmt.executeUpdate();
			}
		} finally {
			DBConnMgr.closeStatement(pstmt);
			//DBConnMgr.closeConnection(con);
			if(con != null) try { con.close(); } catch (SQLException e) {}
		}
	}



	/**
	 * 업체명 구하기
	 */
	public static String getVEND_NAME(String CORP_CODE, String VEND_CODE) throws Exception {
		Connection con = null;
		Statement  stmt = null;
		ResultSet  rs   = null;
		StringBuffer sql = new StringBuffer();
		String VEND_NAME = "";

		try {
			con = DBConnMgr.getMcamsConnection();
			stmt = con.createStatement();

			sql.append("SELECT VDNM_VEND_NAME                       \n");
			sql.append("  FROM MPSFLE.LFRBVDNM                      \n");
			sql.append(" WHERE VDNM_CORP_CODE = '" + CORP_CODE + "' \n");
			sql.append("   AND VDNM_VEND_CODE = '" + VEND_CODE + "' \n");
			sql.append("  WITH UR                                   \n");

			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				VEND_NAME = rs.getString("VDNM_VEND_NAME");
			}
		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
			DBConnMgr.closeConnection(con);
		}

		return VEND_NAME;
	}



	/**
	 * '진행상태코드' 얻기 : SUB
	 */
	public static String getPASS_STAT(Connection con, String CORP_CODE, String PART_SUB0, String EONO_SUB0, String CUMT_SUB0) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		String PASS_STAT = "";

		try {
			sql.append("SELECT (SELECT VALUE(EMST_PASS_STAT,'')              \n");
			sql.append("          FROM MCMFLE.PFRCEMST                       \n");
			sql.append("         WHERE EMST_CORP_CODE = SPMS_CORP_ENDI       \n");
			sql.append("           AND EMST_COST_GUBN = SPMS_COST_ENDI       \n");
			sql.append("           AND EMST_PART_NO   = SPMS_PART_ENDI       \n");
			sql.append("           AND EMST_EONO      = SPMS_EONO_ENDI       \n");
			sql.append("           AND EMST_CUMT_NO   = SPMS_CUMT_ENDI       \n");
			sql.append("       )                                             \n");
			sql.append("  FROM MCMFLE.PFRCSPMS                               \n");
			sql.append(" WHERE SPMS_CORP_CODE = '" + CORP_CODE + "'          \n");
			sql.append("   AND SPMS_PART_NO   = '" + PART_SUB0 + "'          \n");
			sql.append("   AND SPMS_EONO      = '" + EONO_SUB0 + "'          \n");
			sql.append("   AND SPMS_CUMT_NO   =  " + CUMT_SUB0 + "           \n");
			sql.append("  WITH UR                                            \n");

			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				PASS_STAT = rs.getString(1);
			}
		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
		}

		return PASS_STAT;
	}


	/**
	 * '진행상태코드' 얻기 : SUB
	 */
	public static String getPASS_STAT(String CORP_CODE, String PART_SUB0, String EONO_SUB0, String CUMT_SUB0) throws Exception {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		String PASS_STAT = "";

		try {
			sql.append("SELECT (SELECT VALUE(EMST_PASS_STAT,'')              \n");
			sql.append("          FROM MCMFLE.PFRCEMST                       \n");
			sql.append("         WHERE EMST_CORP_CODE = SPMS_CORP_ENDI       \n");
			sql.append("           AND EMST_COST_GUBN = SPMS_COST_ENDI       \n");
			sql.append("           AND EMST_PART_NO   = SPMS_PART_ENDI       \n");
			sql.append("           AND EMST_EONO      = SPMS_EONO_ENDI       \n");
			sql.append("           AND EMST_CUMT_NO   = SPMS_CUMT_ENDI       \n");
			sql.append("       )                                             \n");
			sql.append("  FROM MCMFLE.PFRCSPMS                               \n");
			sql.append(" WHERE SPMS_CORP_CODE = '" + CORP_CODE + "'          \n");
			sql.append("   AND SPMS_PART_NO   LIKE '" + PART_SUB0 + "%'          \n");
			sql.append("   AND SPMS_EONO      = '" + EONO_SUB0 + "'          \n");
			sql.append("   AND SPMS_CUMT_NO   =  " + CUMT_SUB0 + "           \n");
			sql.append("  WITH UR                                            \n");

			con = DBConnMgr.getMcamsConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				PASS_STAT = rs.getString(1);
			}
		} catch(Exception e) {
			System.out.println("# ERROR!!! SQL : " + sql.toString());
			throw e;
		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
			DBConnMgr.closeConnection(con);
		}

		return PASS_STAT;
	}


	/**
	 * '진행상태코드' 얻기 : END
	 */
	public static String getPASS_STAT(Connection con, String CORP_CODE, String COST_ENDI, String PART_ENDI, String EONO_ENDI, String CUMT_ENDI) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		String PASS_STAT = "";

		try {
			sql.append("SELECT MCMFLE.FNRCCMIQ07('" + CORP_CODE + "', '" + COST_ENDI + "', '" + PART_ENDI + "', '" + EONO_ENDI + "', " + CUMT_ENDI + ") \n");
			sql.append("  FROM SYSIBM.SYSDUMMY1                                                                                    \n");
			sql.append("  WITH UR                                                                                                  \n");

			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				PASS_STAT = rs.getString(1);
			}
		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
		}

		return PASS_STAT;
	}



	/**
	 * '빨강색'이 나게끔 문자열 리턴
	 *
	 * <C>코드값</C> =====> <font color='red'>코드값</font>
	 */
	public static String getRedFont(String src) {
		String value = src;

		value = value.replaceAll("<C>", "<font color='red'>");
		value = value.replaceAll("</C>", "</font>");

		return value;
	}



	/**
	 * 다국어명 가져오기
	 */
	public static String getLANG_NAME(String LANG, String CODE) throws Exception {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		String NAME = "";

		if (StringUtils.isBlindSQL(LANG, CODE) == true) {
			throw new Exception("Invalid Parameters. [Blind-SQL Code!!!]");
		}

		try {
			sql.append("SELECT CAST(MCMFLE.FNRMCMIQ02('" + CODE + "', '" + LANG + "') AS VARCHAR(100) CCSID 1208)  \n");
			sql.append("  FROM SYSIBM.SYSDUMMY1                                   \n");
			sql.append("  WITH UR                                                 \n");

			con = DBConnMgr.getMcamsConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				NAME = rs.getString(1);
			}
		} catch(Exception e) {
			System.out.println("# ERROR SQL : \n" + sql);
			throw e;
		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
			DBConnMgr.closeConnection(con);
		}

		return NAME;
	}





	/**
	 * 자동공법 화면 ID 얻기
	 * 예 : "PADD" => "PA2A", "PLAS" => "PA2B", "ST2B" => "PA2U"
	 */
	public static Map<String, String> COME_CODE = null;

	public static String getComeCode(String comeCode) {
		if (COME_CODE == null) {
			COME_CODE = new HashMap<String, String>();

			COME_CODE.put("PRES", "PAA1"); // PRESS
			COME_CODE.put("PLIJ", "PAB0"); // PL사출
			COME_CODE.put("CICT", "PAD0"); // 주철주조
			COME_CODE.put("HEFO", "PAF0"); // 열간단조
			COME_CODE.put("COFO", "PAG1"); // 냉간단조
			COME_CODE.put("STGC", "PAH1"); // STEEL 도금
			COME_CODE.put("STCT", "PAI0"); // STEEL 도장
			COME_CODE.put("PLGC", "PAJ1"); // PL 도금
			COME_CODE.put("PLCT", "PAK1"); // PL 도장
			COME_CODE.put("ALCT", "PAL1"); // AL 중력주조
			COME_CODE.put("DIEC", "PAM1"); // 다이케스팅
			COME_CODE.put("COSP", "PAO1"); // 냉간 SPRING
			COME_CODE.put("BLMD", "PAP0"); // BLOW MOLDING
			COME_CODE.put("WELD", "PAQ1"); // 용접류
			COME_CODE.put("LABL", "PAW1"); // LABEL

			COME_CODE.put("PADD", "PA2A"); // PAD
			COME_CODE.put("PLAS", "PA2B"); // PLASTIC 융착
			COME_CODE.put("PIPE", "PA2C"); // PIPE 가공/인발
			COME_CODE.put("ALFO", "PA2D"); // AL 열간단조
			COME_CODE.put("RUBB", "PA2E"); // RUBBER(INJ/PRS)
			COME_CODE.put("GMTF", "PA2F"); // GMT 성형
			COME_CODE.put("HEAT", "PA2G"); // 열처리
			COME_CODE.put("FILM", "PA2H"); // FILM INSERT
			COME_CODE.put("WPRS", "PA2I"); // 수압전사
			COME_CODE.put("IONP", "PA2J"); // ION PLATING
			COME_CODE.put("HOSE", "PA2K"); // 일반 HOSE
			COME_CODE.put("NUTT", "PA2L"); // NUT
			COME_CODE.put("EPPF", "PA2M"); // EPP 성형
			COME_CODE.put("BOLT", "PA2N"); // BOLT
			COME_CODE.put("CPAD", "PA2O"); // CRASH PAD
			COME_CODE.put("LAMP", "PA2P"); // LAMP ASSY
			COME_CODE.put("SINT", "PA2Q"); // 소결
			COME_CODE.put("FLOC", "PA2R"); // FLOCK'G
			COME_CODE.put("HCSP", "PA2S"); // COIL SPRING
			COME_CODE.put("HFSP", "PA2T"); // 중실 STAB BAR
			COME_CODE.put("HPSP", "PA2U"); // 중공 STAB BAR
			COME_CODE.put("PCBD", "PA3A"); // PCB
			COME_CODE.put("SYMB", "PA3B"); // SYMBOL
			COME_CODE.put("ANOD", "PA3C"); // ANODIZIN

			COME_CODE.put("IPRE", "PA4A"); // 인도 - PRESS
			COME_CODE.put("IPIJ", "PA4B"); // 인도 - PL사출
			COME_CODE.put("IRUB", "PA4C"); // 인도 - RUBBER
			COME_CODE.put("IPCT", "PA4D"); // 인도 - PL도장
			COME_CODE.put("ISCT", "PA4E"); // 인도 - STEEL 도장
			COME_CODE.put("IWEL", "PA4F"); // 인도 - 용접류

			COME_CODE.put("UPRE", "PA5A"); // 북미 - PRESS
			COME_CODE.put("UPIJ", "PA5B"); // 북미 - PL사출
			COME_CODE.put("UBLM", "PA5C"); // 북미 - BLOW MOLDING

		}

		return (String)COME_CODE.get(comeCode);
	}



	/**
	 * 다국어 코드(다중 코드) 메시지 얻기
	 */
	public static String getSPMessage(DataSet ds, String arrayMsg) throws Exception {
		char[] charArray = null;
		StringBuffer sb = new StringBuffer();
		String errFlag = null;
		String LANG_CODE = null;
		String LANG_NAME = null;
		int startIndex = 0;
		int endIndex = 8;

		try {

			if (arrayMsg == null || arrayMsg.equals("")) {
				return "";
			}

			charArray = arrayMsg.toCharArray();

			for (int i=0; i < charArray.length; i++) {
				errFlag = "";
				startIndex = i;

				// =====================================
				//  MESSAGE / LABEL 규칙
				// =====================================
				// 1. 8자의 영숫자로 이루어짐.
				// 2. 첫글자는 M또는 L로 시작.
				// 3. 2~3번째자리는 영숫자 혼용.
				// 4. 4~8자는 반드시 숫자임.

				// 8자리 문자열로 자르기
				// 단, 남은 문자열이 8자리보다 작은경우 그냥 리턴한다.
				if ((charArray.length - startIndex) < endIndex) {
					sb.append(new String(charArray, startIndex, (charArray.length - startIndex)));
					break;
				} else {
					LANG_CODE = new String(charArray, startIndex, endIndex);
				}


				// 8자리 문자가 영어 + 숫자인지 확인
				char[] langArray = LANG_CODE.toCharArray();

				for (int j=0; j < langArray.length; j++) {
					// 영어 + 숫자인경우
					// 영어 : 65 ~ 90 / 숫자 : 48 ~ 57
					if ((langArray[j] <= 90 && langArray[j] >= 65) ||
							(langArray[j] <= 57 && langArray[j] >= 48)
							) {
						errFlag = "S";
						// 아닌경우
					} else {
						errFlag = "E";
						break;
					}
				}

				// 영어 + 숫자가 아닌경우 다음 문자열 찾기
				if (errFlag.equals("E")) {
					sb.append(charArray[i]);
					continue;
				}


				// 첫글짜가 M & L 로 시작하는지 확인
				if (LANG_CODE.startsWith("L") == false && LANG_CODE.startsWith("M") == false) {
					sb.append(charArray[i]);
					continue;
				}

				// 문자가 영어 + 숫자인지 확인
				for (int k=1; k < langArray.length; k++) {

					// 2~3번째자리는 영문 + 숫자만 허용
					if (k == 1 || k == 2) {
						// 영어 + 숫자인경우
						// 영어 : 65 ~ 90 / 숫자 : 48 ~ 57
						if ((langArray[k] <= 90 && langArray[k] >= 65) ||
								(langArray[k] <= 57 && langArray[k] >= 48)
								) {
							errFlag = "S";
							continue;
							// 아닌경우
						} else {
							errFlag = "E";
							break;
						}
						// 4~8번째자리는 숫자만 허용
					} else {
						// 숫자인경우
						if (Character.isDigit(langArray[k])) {
							errFlag = "S";
							continue;
							// 아닌경우
						} else {
							errFlag = "E";
							break;
						}
					}
				}

				// 영어 + 숫자가 아닌경우 다음 문자열 찾기
				if (errFlag.equals("E")) {
					sb.append(charArray[i]);
					continue;
				}

				// 모든 규칙은 만족하는 경우 정상적인 다국어 코드로 인정
				if (LANG_CODE.startsWith("L")) {
					LANG_NAME = ds.L.get(LANG_CODE);
				} else if (LANG_CODE.startsWith("M")) {
					LANG_NAME = ds.M.get(LANG_CODE);
				}

				// 다국어가 없는경우 코드값을 설정한다.
				if (LANG_NAME.equals("") || LANG_NAME == null) {
					sb.append(LANG_CODE);
				} else {
					sb.append(LANG_NAME);
				}


				// 정상적인 다국어 코드인경우 다음 8자리를 찾는다.
				i = i + 7;
			}
		} catch(Exception e) {
			//에러 발생 시 그대로 리턴
			return arrayMsg;
		}

		return sb.toString();

	}


	/**
	 * RFQ No 형태로 변환
	 */
	public static String changeRfqNoType(String rfqNo) {
		if (rfqNo.length() != 12) {
			return rfqNo;
		}

		StringBuffer value = new StringBuffer("");
		value.append(rfqNo.substring( 0,  4));
		value.append("-");
		value.append(rfqNo.substring( 4, 10));
		value.append("-");
		value.append(rfqNo.substring(10, 12));

		return value.toString();
	}


	/**
	 * 값이 양수이면 RED FONT / 음수이면 BLUE 폰트 처리
	 */
	public static String changeNumberFontColor(String num) {
		String color = "";
		String tempValue = num;

		tempValue = StringUtils.replace(num, "," , "");

		// 숫자 타입이 아니면 "" 리턴
		if (StringUtils.isNumeric(tempValue) == false) {
			return "";
		}

		if (Double.parseDouble(tempValue) == 0) {
			return "";

		} else if (Double.parseDouble(tempValue) > 0) {
			color = "red";

		} else {
			color = "blue";
		}

		return "<font color='" + color + "'>" + num + "</font>";
	}



	/**
	 * 문제 품목으로 등록 되었는가? (ALL0/END0/SUB0/COME)
	 */
	public static String getBAD0_CODE(Connection con, String CHCK_CODE, String CORP_CODE, String COST_GUBN, String PART_NO, String EONO, int CUMT_NO, String COME_CODE) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		String BAD0_CODE = "";    

		try {
			sql.append("SELECT MCMFLE.FNRCCMIQ23             \n");
			sql.append("       (                             \n");
			sql.append("          '" + CHCK_CODE + "'        \n");
			sql.append("         ,'" + CORP_CODE + "'        \n");
			sql.append("         ,'" + COST_GUBN + "'        \n");
			sql.append("         ,'" + PART_NO   + "'        \n");
			sql.append("         ,'" + EONO      + "'        \n");
			sql.append("         , " + CUMT_NO   + "         \n");
			sql.append("         ,'" + COME_CODE + "'        \n");
			sql.append("       )                             \n");
			sql.append("  FROM SYSIBM.SYSDUMMY1              \n");
			sql.append("  WITH UR                            \n");

//			System.out.println(sql);
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				BAD0_CODE = rs.getString(1);
			}
		} catch(Exception e) {
			System.out.println("# ERROR!!! SQL : \n" + sql.toString());
			throw e;
		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
		}

		return BAD0_CODE;
	}




	/**
	 * 문제 품목으로 등록된 사유 구하기 (END0/SUB0/COME)
	 */
	public static String getBAD0_REMK(Connection con, String CHCK_CODE, String CORP_CODE, String COST_GUBN, String PART_NO, String EONO, int CUMT_NO, String COME_CODE) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		String BAD0_REMK = "";

		try {
			sql.append("SELECT MCMFLE.FNRCCMIQ24             \n");
			sql.append("       (                             \n");
			sql.append("          '" + CHCK_CODE + "'        \n");
			sql.append("         ,'" + CORP_CODE + "'        \n");
			sql.append("         ,'" + COST_GUBN + "'        \n");
			sql.append("         ,'" + PART_NO   + "'        \n");
			sql.append("         ,'" + EONO      + "'        \n");
			sql.append("         , " + CUMT_NO   + "         \n");
			sql.append("         ,'" + COME_CODE + "'        \n");
			sql.append("       )                             \n");
			sql.append("  FROM SYSIBM.SYSDUMMY1              \n");
			sql.append("  WITH UR                            \n");

			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				BAD0_REMK = rs.getString(1);
			}
		} catch(Exception e) {
			System.out.println("# ERROR!!! SQL : \n" + sql.toString());
			throw e;
		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(stmt);
		}

		return BAD0_REMK;
	}



	/**
	 * 법인에 따른 '건물비'
	 * 2011.12.22 HJJ : 중국법인 변경 (CNS1 --> 2400, CNS2 --> 2600, CNS3 --> 2500, CNS4 --> 사용안됨
	 */
	public static String get_BULD_COST(String CORP) {
		String BULD_COST = "";

		if (CORP != null && (CORP.equals("1000") || CORP.equals("9900"))) {
			BULD_COST = "160000";
		} else if(CORP.equals("CNAL") ||
				CORP.equals("CNB1") ||
				CORP.equals("CNB2") ||
				CORP.equals("CNB3") ||
				CORP.equals("2100") ||
				CORP.equals("2300") ||
				CORP.equals("2400") ||
				CORP.equals("2600") ||
				CORP.equals("2500") ||
				CORP.equals("2900") ||
				CORP.equals("C100") ||
				CORP.equals("C300") ||
				CORP.equals("C400") ||
				CORP.equals("C600") ||
				CORP.equals("C500") ||
				CORP.equals("C900") ||                 
				CORP.equals("2A00") //2016.09.29 창주 법인 ADD
				) {
			BULD_COST = "1525";
		}

		return BULD_COST;
	}




	/**
	 * 법인에 따른 '전력단가'
	 * 2011.12.22 HJJ : 중국법인 변경 (CNS1 --> 2400, CNS2 --> 2600, CNS3 --> 2500, CNS4 --> 사용안됨
	 */
	public static String get_ELEC_PRIC(String CORP) {
		String ELEC_PRIC = "";

		if (CORP != null && (CORP.equals("1000") || CORP.equals("9900"))) {
			ELEC_PRIC = "72.0";
		} else if(CORP.equals("CNAL") ||
				CORP.equals("CNB1") ||
				CORP.equals("CNB2") ||
				CORP.equals("CNB3") ||
				CORP.equals("2100") ||
				CORP.equals("2300") ||
				CORP.equals("2400") ||
				CORP.equals("2600") ||
				CORP.equals("2500") ||
				CORP.equals("2900") ||
				CORP.equals("C100") ||
				CORP.equals("C300") ||
				CORP.equals("C400") ||
				CORP.equals("C600") ||
				CORP.equals("C500") ||
				CORP.equals("C900") ||
				CORP.equals("2A00") //2016.09.29 창주 법인 ADD
				) {
			ELEC_PRIC = "0.7";
		}

		return ELEC_PRIC;
	}




	/**
	 * 법인에 따른 소수점 이하 사용 자리수 리턴
	 * 2011.06.22 HJJ : 소수점 자리수 4 --> 6
	 */
	public static int getCipherSize(String CORP) {
		int size = 6;

		if (CORP != null && (CORP.equals("1000") || CORP.equals("9900"))) {
			size = 2;
		}

		return size;
	}



	/**
	 * 소수점 이하에서 뒤에 붙은 0들을 하나만 남기고 제거한다.(MCAMS에서만 사용되는 규칙임)
	 *
	 * e.g : 1.050000 ==> 1.05
	 * e.g : 1.0      ==> 1.0
	 * 주의 : 본 method는 MOBISBaseDAO의 복사본 이므로 절대 수정해서는 안된다.
	 */
	public static String trimZero(String str) {
		char c = 0;
		int end_pos = 0;
		String returnValue = "";

		if (str == null) {
			str = "";
		}

		if (str.indexOf(".") == -1) {
			returnValue = str;
		} else {
			for (int i=str.length()-1; i >=0 ; i--) {
				c = str.charAt(i);

				if (c == '0') {
					continue;
				} else if (c == '.') {
					end_pos = i + 2;
					break;
				} else {
					end_pos = i + 1;
					break;
				}
			}

			returnValue = str.substring(0, end_pos);
		}

		return returnValue;

	}


	/**
	 * 해당 법인에 맞는 자리수를 뒤에 0을 잘라내고 리턴
	 *
	 * 예) 123.456000 ===> 123.456 (중국)
	 * 예) 123.456000 ===> 123.45  (본사)
	 *
	 * 주의 : 이곳을 수정했으면 McamsUtil.java에 같은 method있으므로 동일하게 변경해줘야 한다.
	 *      : 단, McamsUtil.resultChange()는 static method 이다.
	 * 2011.06.22 HJJ : 인도, 북미 법인추가, 본사를 제외한 법인 소수점 6자리로 변경
	 * 2011.12.22 HJJ : 중국법인 변경 (CNS1 --> 2400, CNS2 --> 2600, CNS3 --> 2500, CNS4 --> 사용안됨
	 * 2013.07.25 PMS : 중국 (구) 법인 추가 
	 */
	public static String resultChange(String val, String CORP_CODE) throws Exception {
		String returnVal = "";
		java.text.DecimalFormat df = null;

		if (val == null) {
			val = "0.00";
		} else if (val.trim().equals("")) {
			val = "0.00";
		}

		val = val.replaceAll(",", "");

		df = new java.text.DecimalFormat("###0.00000000000000000");
		val = df.format(df.parse(val));


		int point_index = val.indexOf(".");

		if (CORP_CODE.equals("1000") || CORP_CODE.equals("9900") || CORP_CODE.equals("1400") || CORP_CODE.equals("1300")) {
			val = val.substring(0, point_index) + val.substring(point_index, point_index + 3);
			df = new java.text.DecimalFormat("###0.0#");
			// 중국법인
		} else if(CORP_CODE.equals("CNAL") ||
				CORP_CODE.equals("CNB1") ||
				CORP_CODE.equals("CNB2") ||
				CORP_CODE.equals("CNB3") ||
				CORP_CODE.equals("2100") ||
				CORP_CODE.equals("2300") ||
				CORP_CODE.equals("2400") ||
				CORP_CODE.equals("2600") ||
				CORP_CODE.equals("2500") ||
				CORP_CODE.equals("2900") ||
				CORP_CODE.equals("C100") ||
				CORP_CODE.equals("C300") ||
				CORP_CODE.equals("C400") ||
				CORP_CODE.equals("C600") ||
				CORP_CODE.equals("C500") ||
				CORP_CODE.equals("C900") ||
				CORP_CODE.equals("2A00") //2016.09.29 창주 법인 ADD

				) {
			val = val.substring(0, point_index) + val.substring(point_index, point_index + 7);
			df = new java.text.DecimalFormat("###0.0#####");
			// 인도법인
		} else if(CORP_CODE.equals("INAL") ||
				CORP_CODE.equals("4200") || CORP_CODE.equals("4700")
				) {
			val = val.substring(0, point_index) + val.substring(point_index, point_index + 7);
			df = new java.text.DecimalFormat("###0.0#####");
			// 북미법인
		} else if(CORP_CODE.equals("USAL") ||
				CORP_CODE.equals("5100") ||
				CORP_CODE.equals("5600")
				) {
			val = val.substring(0, point_index) + val.substring(point_index, point_index + 7);
			df = new java.text.DecimalFormat("###0.0#####");
			// 체코
		} else if(CORP_CODE.equals("6500")) {
			val = val.substring(0, point_index) + val.substring(point_index, point_index + 7);
			df = new java.text.DecimalFormat("###0.0#####");
			// 슬로바키아
		} else if(CORP_CODE.equals("6300")) {
			val = val.substring(0, point_index) + val.substring(point_index, point_index + 7);
			df = new java.text.DecimalFormat("###0.0#####");
			// 브라질
		} else if(CORP_CODE.equals("5700")) {
			val = val.substring(0, point_index) + val.substring(point_index, point_index + 7);
			df = new java.text.DecimalFormat("###0.0#####");
			// 러시아
		} else if(CORP_CODE.equals("6600")) {
			val = val.substring(0, point_index) + val.substring(point_index, point_index + 7);
			df = new java.text.DecimalFormat("###0.0#####");
			// 멕시코
		} else if(CORP_CODE.equals("5B00")) {
			val = val.substring(0, point_index) + val.substring(point_index, point_index + 7);
			df = new java.text.DecimalFormat("###0.0#####");			
		} else {
			throw new Exception("NOT VALID CORPERATION CODE!!");
		}

		returnVal = df.format(df.parse(val));

		return returnVal;
	}



	/**
	 * 통합 유효성 검사
	 *
	 * 1. '문제품목'          : BAD0
	 * 2. '품의중복'          : REPT
	 * 3. '재질구분(T)'       : MTLT
	 * 4. '임시재료단가 사용' : IMSI
	 */
	public static DataSet integrateCheck(Connection con, String COMH_EMPL_NUMB, String COMC_CHCK_TYPE, String COMC_RETN_TYPE, String COMC_CORP_CODE, String COMC_COST_GUBN, String COMC_PART_NO, String COMC_EONO, int COMC_CUMT_NO) throws Exception {
		int c = 1;
		int o = 10;
		DataSet ds = new DataSet();
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		CallableStatement cstmt = null;

		ds.set("COMH_EMPL_NUMB", COMH_EMPL_NUMB);
		ds.set("COMC_CHCK_TYPE", COMC_CHCK_TYPE);
		ds.set("COMC_RETN_TYPE", COMC_RETN_TYPE);
		ds.set("COMC_CORP_CODE", COMC_CORP_CODE);
		ds.set("COMC_COST_GUBN", COMC_COST_GUBN);
		ds.set("COMC_PART_NO  ", COMC_PART_NO  );
		ds.set("COMC_EONO     ", COMC_EONO     );
		ds.set("COMC_CUMT_NO  ", COMC_CUMT_NO  );


		try {
			//============================================== 1 2 3 4 5 6 7 8 9 0 1 2 3
			cstmt = con.prepareCall("{call MCMFLE.SPRC00IQ01(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

			cstmt.setString           (c++, COMH_EMPL_NUMB);
			cstmt.setString           (c++, "KO");
			cstmt.setString           (c++, COMC_CHCK_TYPE);
			cstmt.setString           (c++, COMC_RETN_TYPE);
			cstmt.setString			  (c++, COMC_CORP_CODE);
			cstmt.setString			  (c++, COMC_COST_GUBN);
			cstmt.setString			  (c++, COMC_PART_NO  );
			cstmt.setString			  (c++, COMC_EONO     );
			cstmt.setInt			  (c++, COMC_CUMT_NO  );
			cstmt.registerOutParameter(c++, Types.VARCHAR );
			cstmt.registerOutParameter(c++, Types.VARCHAR );
			cstmt.registerOutParameter(c++, Types.VARCHAR );
			cstmt.registerOutParameter(c++, Types.VARCHAR );
			cstmt.registerOutParameter(c++, Types.VARCHAR );
			cstmt.registerOutParameter(c++, Types.VARCHAR );

			rs = cstmt.executeQuery();

			c = o;
			ds.set("COMT_MESG_CODE", cstmt.getString(c++));
			ds.set("COMT_MESG     ", McamsUtil.getSPMessage(ds,cstmt.getString(c++)));
			ds.set("COMT_SQL0_CODE", cstmt.getString(c++));
			ds.set("COMT_PRGM_ID  ", cstmt.getString(c++));
			ds.set("COMT_LINE_NO  ", cstmt.getString(c++));

			logger.info(cstmt.getString(15));

			// 오류 발생
			if (ds.get("COMT_MESG_CODE").equals("IQ-001") == false) {
				// 업무 오류
				if (ds.get("COMT_MESG_CODE").equals("IQ-002")) {
					throw new BusinessException(ds.get("COMT_MESG"));
					// 시스템 오류
				} else {
					ds.print();
					throw new Exception(ds.get("COMT_MESG"));
				}
			}

			// 카운트인 경우
			if (COMC_RETN_TYPE.equals("CNT0")) {
				if (rs.next()) {
					ds.set("COMD_EROR_CNT0", rs.getInt(1));
				}
				// list인 경우
			} else if (COMC_RETN_TYPE.equals("LIST")) {

				rsmd = rs.getMetaData();
				int col_cnt = rsmd.getColumnCount();
				String[] cols = new String[col_cnt];
				for (int i=1; i <= col_cnt; i++) {
					cols[i-1] = rsmd.getColumnName(i);
				}

				while (rs.next()) {
					for (int i=0; i < cols.length; i++) {
						ds.add(cols[i], rs.getString(cols[i]));
					}
				}

				ds.page.REAL_ROWS = ds.size(cols[0]);
			}

		} finally {
			DBConnMgr.closeResultSet(rs);
			DBConnMgr.closeStatement(cstmt);
		}

		return ds;
	}



	/**
	 * 오류 발생시 XML PARAMETER를 로그에 출력한다.
	 */
	public static void printXmlParameter(HttpServletRequest req) {
		int    DELI_INDX = 0;
		String DELI_STR0 = "    : ";
		StringBuffer sb = new StringBuffer("\r\n# ERROR : \r\n");
		String param = mobis.common.base.ServletHelper.getRequestInfo(req).toString();
		String[] arrParam = null;

		param = param.replaceAll(", ", " \r\n");
		param = param.replaceAll("</([[a-z0-9A-Z_]]+)>", "");
		param = param.replaceAll("<", "\r\n# ");
		param = param.replaceAll(">", DELI_STR0);
		arrParam = param.split("\r\n");

		for (int i=0; i < arrParam.length; i++) {
			DELI_INDX = arrParam[i].indexOf(DELI_STR0);

			// XML PARAMETER인 경우
			if (DELI_INDX != -1) {
				String NAME = arrParam[i].substring(0, DELI_INDX);

				// NAME이 짧은 경우 스페이스를 채운다.
				if (NAME.length() < 16) {
					NAME = StringUtils.appendCharAtTail(NAME, ' ', 16);
					arrParam[i] = NAME + arrParam[i].substring(DELI_INDX);
				}
			}

			sb.append(arrParam[i] + "\r\n");
		}

		System.err.println(sb);
	}



	public static String getSPMessageNew(LangMap L, LangMap M, String arrayMsg) {
		char[] charArray = null;
		StringBuffer sb = new StringBuffer();
		String errFlag = null;
		String LANG_CODE = null;
		String LANG_NAME = null;
		int startIndex = 0;
		int endIndex = 8;

		try {

			if (arrayMsg == null || arrayMsg.equals("")) {
				return "";
			}

			charArray = arrayMsg.toCharArray();

			for (int i=0; i < charArray.length; i++) {
				errFlag = "";
				startIndex = i;

				// =====================================
				//  MESSAGE / LABEL 규칙
				// =====================================
				// 1. 8자의 영숫자로 이루어짐.
				// 2. 첫글자는 M또는 L로 시작.
				// 3. 2~3번째자리는 영숫자 혼용.
				// 4. 4~8자는 반드시 숫자임.

				// 8자리 문자열로 자르기
				// 단, 남은 문자열이 8자리보다 작은경우 그냥 리턴한다.
				if ((charArray.length - startIndex) < endIndex) {
					sb.append(new String(charArray, startIndex, (charArray.length - startIndex)));
					break;
				} else {
					LANG_CODE = new String(charArray, startIndex, endIndex);
				}


				// 8자리 문자가 영어 + 숫자인지 확인
				char[] langArray = LANG_CODE.toCharArray();

				for (int j=0; j < langArray.length; j++) {
					// 영어 + 숫자인경우
					// 영어 : 65 ~ 90 / 숫자 : 48 ~ 57
					if ((langArray[j] <= 90 && langArray[j] >= 65) ||
							(langArray[j] <= 57 && langArray[j] >= 48)
							) {
						errFlag = "S";
						// 아닌경우
					} else {
						errFlag = "E";
						break;
					}
				}

				// 영어 + 숫자가 아닌경우 다음 문자열 찾기
				if (errFlag.equals("E")) {
					sb.append(charArray[i]);
					continue;
				}


				// 첫글짜가 M & L 로 시작하는지 확인
				if (LANG_CODE.startsWith("L") == false && LANG_CODE.startsWith("M") == false) {
					sb.append(charArray[i]);
					continue;
				}

				// 문자가 영어 + 숫자인지 확인
				for (int k=1; k < langArray.length; k++) {

					// 2~3번째자리는 영문 + 숫자만 허용
					if (k == 1 || k == 2) {
						// 영어 + 숫자인경우
						// 영어 : 65 ~ 90 / 숫자 : 48 ~ 57
						if ((langArray[k] <= 90 && langArray[k] >= 65) ||
								(langArray[k] <= 57 && langArray[k] >= 48)
								) {
							errFlag = "S";
							continue;
							// 아닌경우
						} else {
							errFlag = "E";
							break;
						}
						// 4~8번째자리는 숫자만 허용
					} else {
						// 숫자인경우
						if (Character.isDigit(langArray[k])) {
							errFlag = "S";
							continue;
							// 아닌경우
						} else {
							errFlag = "E";
							break;
						}
					}
				}

				// 영어 + 숫자가 아닌경우 다음 문자열 찾기
				if (errFlag.equals("E")) {
					sb.append(charArray[i]);
					continue;
				}

				// 모든 규칙은 만족하는 경우 정상적인 다국어 코드로 인정
				if (LANG_CODE.startsWith("L")) {
					LANG_NAME = L.get(LANG_CODE);
				} else if (LANG_CODE.startsWith("M")) {
					LANG_NAME = M.get(LANG_CODE);
				}

				// 다국어가 없는경우 코드값을 설정한다.
				if (LANG_NAME.equals("") || LANG_NAME == null) {
					sb.append(LANG_CODE);
				} else {
					sb.append(LANG_NAME);
				}


				// 정상적인 다국어 코드인경우 다음 8자리를 찾는다.
				i = i + 7;
			}
		} catch(Exception e) {
			//에러 발생 시 그대로 리턴
			return arrayMsg;
		}

		return sb.toString();
	}

}
