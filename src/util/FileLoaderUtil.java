package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import model.dao.CommonDAO;


/**
 * property 파일과 xml 문서를 로드하기 위한 util 클래스
 * 싱글톤 디자인 패턴을 가지고 있고, 한번의 인스턴스만 생성
 * 로드된 property랑 xml문서는 캐시
 * @author indi
 *
 */
public class FileLoaderUtil {
	/**
	 * 인스턴스를 저장하는 정적 변수
	 */
	private static FileLoaderUtil instance = new FileLoaderUtil();
	
	/**
	 * msg property객체들을 저장하는 멤버 변수
	 */
	private Properties msgProp;
	
	/**
	 * config property 객체들을 저장하는 멤버 변수
	 */
	private Properties configProp;
	
	/**
	 * crawlerTag property 객체들을 저장하는 멤버 변수
	 */
	private Properties crawlerTagProp;
	
	/**
	 * xml 객체랑 문서들을 저장하는 멤버 변수
	 */
	private Document document;

	/**
	 * 생성자
	 * private으로 선언해서 다른 클래스에서 인스턴스 생성 못하게 함
	 */
	private FileLoaderUtil() {
		msgProp = new Properties();
		configProp = new Properties();
		crawlerTagProp= new Properties();
		CommonDAO.loadSqlMap();
	}

	/**
	 * 유일한 인스턴스를 반환하는 메서드
	 * @return
	 */
	public static FileLoaderUtil getInstance() {
		return instance;
	}
	
	/**
	 * crawlerTag.properties 파일을 로드해서 반환하는 메서드
	 * @return properties
	 */
	public Properties getCrawlerTagProperties() {
		if(crawlerTagProp.isEmpty()) {
			loadProperties(crawlerTagProp,"config/crawlerTag.properties");
		}
		System.out.println("crawlingLoader: "+crawlerTagProp);
		return crawlerTagProp;
	}
	
	/**
	 * msg.properteis 파일을 로드해서 반환하는 메서드
	 * @return properties
	 */
	public Properties getMsgProperties() {
		if (msgProp.isEmpty()) {
			loadProperties(msgProp, "config/msg.properties");
		}
		return msgProp;
	}

	/**
	 * config.properties 파일을 로드해서 반환하는 메서드
	 * @return properties
	 */
	public Properties getConfigProperties() {
		if (configProp.isEmpty()) {
			loadProperties(configProp, "config/config.properties");
		}
		return configProp;
	}

	/**
	 * action.xml 파일을 로드해서 반환하는 메서드
	 * @return document
	 */
	public Document getDocument() {
		if (document == null) {
			loadXMLDocument("config/action.xml");
		}
		return document;
	}

	/**
	 * 프로퍼티 파일을 로드하는 메서드
	 * @param properties
	 * @param filePath
	 */
	private void loadProperties(Properties properties, String filePath) {
		try (InputStream inputStream = FileLoaderUtil.class.getClassLoader().getResourceAsStream(filePath);
				InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
			properties.load(reader);
		} catch (IOException e) {
			System.err.println("파일 로드 실패: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * xml 파일을 로드하는 메서드
	 * @param filePath
	 */
	private void loadXMLDocument(String filePath) {
		try (InputStream inputStream = FileLoaderUtil.class.getClassLoader().getResourceAsStream(filePath)) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			// xml 문서를 파싱해서 Document 객체로 변환
			document = builder.parse(inputStream);
		} catch (Exception e) {
			System.err.println("XML 파일 로드 실패: " + e.getMessage());
			System.exit(1);
		}
	}
}
