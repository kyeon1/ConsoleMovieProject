package model.dao;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import util.DbUtil;

/**
 * 반복되는 executeUpdate, executeQuery 별도로 분리
 * @author indi
 *
 */
public class CommonDAO {
	
	/**
	 * SQL문 매핑
	 */
	private static final Map<String, String> SQL_MAP = new HashMap<>();
	
	/**
	 *  sql 파일 찾기 & sql문 가져오기
	 */
	public static void loadSqlMap() {
		//파일 입력 스트림 생성
		try (InputStream InputStream = CommonDAO.class.getClassLoader().getResourceAsStream("config/sql.xml")) {
			//xml 문서를 파싱하기 위해 DocumentBuilderFactory 생성 및 인스턴스화
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			//xml문서 파싱해서 Document 객체에 생성
			Document doc = dBuilder.parse(InputStream);
			//xml문서 정규화
			doc.getDocumentElement().normalize();

			//sql 태그를 가진 요소들을 가져옴
			NodeList nodeList = doc.getElementsByTagName("sql");
			if (nodeList != null && nodeList.getLength() > 0) {
				for (int i = 0; i < nodeList.getLength(); i++) {
					Element element = (Element) nodeList.item(i);
					//요소의 id 속성을 키로, 요소의 내용을 값으로 맵에 추가
					SQL_MAP.put(element.getAttribute("id"), element.getTextContent());
				}
			}
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		}
	}
	
	/**
	 * 단일 트랜잭션 executeUpdate
	 * @param conn
	 * @param query			:쿼리문
	 * @param parameters	:파라미터
	 * @return boolean
	 * @throws SQLException	:예외 처리
	 */
	public boolean executeUpdate(Connection conn,String queryKey, Object... parameters) throws SQLException{
		int res = -1;
		String query=SQL_MAP.get(queryKey);
		try {
			try (PreparedStatement pstmt = conn.prepareStatement(query)) {
				int index = 1;
				for (Object param : parameters) {
					pstmt.setObject(index++, param);
				}
				res = pstmt.executeUpdate();
			}
			return res > 0;
		} catch (SQLException e) {
			System.out.println("SQLException1: "+e.getMessage());
			throw e;
		} 
	}

	/**
	 * insert/delete/update DAO
	 * @param query			:쿼리문
	 * @param parameters	:파라미터
	 * @return boolean
	 */
	public boolean executeUpdate(String queryKey, Object... parameters) {
		int res = -1;
		Connection conn = null;
		String query=SQL_MAP.get(queryKey);
		try {
			conn = DbUtil.connect();
			conn.setAutoCommit(false);

			try (PreparedStatement pstmt = conn.prepareStatement(query)) {
				int index = 1;
				for (Object param : parameters) {
					pstmt.setObject(index++, param);
				}

				res = pstmt.executeUpdate();
			}

			conn.commit();
			return res > 0;
		} catch (SQLException e) {
			System.out.printf("SQLException: "+ e.getMessage());
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e2) {
					System.out.printf("롤백 실패: "+e2.getMessage());
				}
			}
			return false;
		} finally {
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					System.out.println("AutoCommit설정해제, 커넥션닫기 실패: "+e.getMessage());
				}
			}
		}
	}



	/**
	 * select DAO
	 * @param <T>
	 * @param query			:쿼리문
	 * @param voClass		:VO클래스
	 * @param parameters	:파라미터
	 * @return List
	 */
	public <T> List<T> executeQuery(String queryKey, Class<T> voClass, Object... parameters) {
		String query=SQL_MAP.get(queryKey);
		try (Connection conn = DbUtil.connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
			int index = 1;
			for (Object param : parameters) {
				pstmt.setObject(index++, param); // 파라미터 설정
			}
			List<T> dataList = new ArrayList<>();
			Field[] fields = voClass.getDeclaredFields(); // vo 클래스 필드 배열 가져옴
			ResultSet rs = pstmt.executeQuery(); // 쿼리 실행한 결과를 rs로 받음
			while (rs.next()) {
				T data = voClass.getDeclaredConstructor().newInstance(); // 생성자를 가져오고 인스턴스 만듬

				for (Field field : fields) {
					String fieldName = field.getName(); // 필드 이름 가져오기
					Object columnValue = null;// 값 가져오기
					try {
						if (field.getType().isAssignableFrom(LocalDate.class)) {
							// 필드가 LocalDate 타입인 경우, DATE 값을 LocalDate로 변환
							java.sql.Date sqlDate = rs.getDate(fieldName);
							if (sqlDate != null) {
								columnValue = sqlDate.toLocalDate();
							}
						} else {
							columnValue = rs.getObject(fieldName);
						}
					} catch (Exception e) {
						//System.err.println("executeQueryError: "+e.getMessage());
					}
					if (columnValue == null) { // 필드값이 null이면 건너뛰기
						continue;
					}
					String setterMethodName = getSetterMethodName(fieldName);
					Method setterMethod = voClass.getMethod(setterMethodName, field.getType());
					setterMethod.invoke(data, columnValue);
				}
				dataList.add(data);
			}
			return dataList;
		} catch (SQLException | InstantiationException | IllegalAccessException
				| InvocationTargetException | NoSuchMethodException e) {
			System.out.println("Exception occurred: "+ e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * setter 메서드 이름을 반환하는 메서드
	 * @param fieldName 필드 이름
	 * @return 세터 메서드 이름
	 */
	private String getSetterMethodName(String fieldName) {
		//필드 이름의 첫 글자를 대문자로 변환
		char firstChar = fieldName.charAt(0);
		firstChar = Character.toUpperCase(firstChar);
		return "set" + firstChar + fieldName.substring(1);
	}
}
