package controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import util.FileLoaderUtil;

public class Controller {
	
	/**
	 * 별도로 분리해논 action.xml 초기화
	 */
	private static Document document=FileLoaderUtil.getInstance().getDocument();
	
	/**
	 * 
	 */
	public Controller() {

	}

	/**
	 * 동작을 수행
	 * @param actionKey	동작 키
	 */
	public void setAction(String actionKey) {
		
		//xml 문서에서 action 요소를 가져옴
		NodeList actionList = document.getElementsByTagName("action");
		for (int i = 0; i < actionList.getLength(); i++) {
			Element actionElement = (Element) actionList.item(i);
			String id = actionElement.getAttribute("id");
			if (id.equals(actionKey)) {
				//action 요소의 속성과 내용을 추출
				String packageName = getTextContent(actionElement, "package");
				String className = getTextContent(actionElement, "class");
				String methodName = getTextContent(actionElement, "method");
				//동작 실행
				executeAction(packageName, className, methodName);
				return;
			}
		}
		System.out.println("Invalid actionKey");
	}

	/**
	 * 동작 실행
	 * @param packageName	패키지 이름
	 * @param className		클래스 이름
	 * @param methodName	메서드 이름
	 */
	private void executeAction(String packageName, String className, String methodName) {
		try {
			Class<?> cls = Class.forName(packageName + "." + className);
			//클래스의 인스턴스 생성
			Object obj = cls.getDeclaredConstructor().newInstance();
			//메서드 가져옴
			Method method = cls.getMethod(methodName);
			//메서드 실행
			method.invoke(obj);
		}catch (Exception e) {
	        if (e instanceof InvocationTargetException) {
	            Throwable cause = ((InvocationTargetException) e).getTargetException();
	            cause.printStackTrace();
	        } else {
	            e.printStackTrace();
	        }
		}
	}

	/**
	 * 요소의 텍스트 가져옴
	 * @param element	요소
	 * @param tagName	태그
	 * @return 태그의 텍스트 내용
	 */
	private String getTextContent(Element element, String tagName) {
		//요소에서 지정된 태그 이름의 요소들 가져옴
		NodeList nodeList = element.getElementsByTagName(tagName);
		if (nodeList.getLength() > 0) {
			//첫번째 요소의 텍스트 내용 반환
			Element tagElement = (Element) nodeList.item(0);
			return tagElement.getTextContent();
		}
		return "";
	}
}