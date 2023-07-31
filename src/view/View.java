package view;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.Controller;
import model.vo.MovieVO;

/**
 * MemberView 클래스
 * 
 * @author indi
 *
 */
public class View {
	
	/**
	 * 
	 */
	private Scanner sc = new Scanner(System.in);
	private Controller ctrl=new Controller();

	/**
	 * 
	 */
	public View() {
		
	}

	/**
	 * String 타입이랑 int 타입을 입력하는 메서드
	 * 
	 * @param msg
	 * @return String
	 */
	public String getPromptStr(String msg) {
		System.out.print(msg + " ▶ ");
		return sc.nextLine();
	}
	
	/**
	 * 메세지 츌력만 하는 매서드
	 * 
	 * @param msg
	 * @return String
	 */
	public String getPropmptMsg(String msg) {
		System.out.println(msg);
		return msg;
	}
	
	/**
	 * 메뉴 번호 입력하는 메서드
	 * controller에 값 넘겨줌
	 * @param msg
	 */
	public void getPromptAction(String msg) {
		String action=getPromptStr(msg);
		ctrl.setAction(action);
	}
	
	/**
	 * 영화 목록
	 * 
	 * @param datas
	 */
	public void printMovieList(List<MovieVO> datas) {
		System.out.printf("\n%-4s  %-15s %-9s %-60s\n", "No.", "개봉 날짜", "평점", "TITLE");
		System.out.println("----------------------------------------------------------");

		for (int i = 0; i < datas.size(); i++) {
			int num = datas.get(i).getMovieNum();
			String title = datas.get(i).getMovieTitle();
			LocalDate date = datas.get(i).getMovieDate();
			Double rate = datas.get(i).getMovieRate();
			System.out.printf("%-5s %-16s %-10s %-60s\n", num, date, rate, title);
		}
	}

	/**
	 * 영화 상세
	 * 
	 * @param data
	 */
	public void printMovieDetail(MovieVO data) {
		System.out.println("[번호] " + data.getMovieNum());
		System.out.println("[개봉 날짜] " + data.getMovieDate());
		System.out.println("[영화 제목] " + data.getMovieTitle());
		String content = data.getMovieContent();
		Pattern pattern = Pattern.compile(".{1,45}(\\s|$)");
		Matcher matcher = pattern.matcher(content);
		System.out.print("[줄거리]");
		while (matcher.find()) {
			System.out.println(" " + matcher.group().trim());
		}
	}
}
