package service;

import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

import crawler.Crawling;
import model.dao.MovieDAO;
import model.vo.MovieVO;
import util.FileLoaderUtil;
import view.View;

/**
 * MoviceService 클래스
 * @author indi
 *
 */
public class MovieService{

	private View view=new View();
	private MovieDAO movieDao=new MovieDAO();
	private static Properties msgProp=FileLoaderUtil.getInstance().getMsgProperties();
	
	/**
	 * 생성자
	 */
	public MovieService() {

	}
	
	/**
	 * 처음 client에서 시작을 도와주는 메서드
	 */
	public void start() {
		Crawling.crawling(movieDao);
		view.getPromptAction(msgProp.getProperty("mainMenu"));
	}
	/**
	 * 영화 목록
	 */
	public void list() {
		List<MovieVO> datas=movieDao.list(null);
		
		if(datas == null) {
			view.getPropmptMsg(msgProp.getProperty("error"));
		}else {
			view.printMovieList(datas);
			view.getPromptAction(msgProp.getProperty("mainMenu"));
		}
	}

	/**
	 * 영화 상세
	 */
	public void detail() {
		MovieVO movieVo =new MovieVO();
		
		int num=Integer.parseInt(view.getPromptStr(msgProp.getProperty("promptMovieNum")));
		movieVo.setMovieNum(num);
		
		MovieVO data=movieDao.select(movieVo).get(0);
		
		if(data == null) {
			view.getPropmptMsg(msgProp.getProperty("error"));
		}else {
			view.printMovieDetail(data);
			view.getPromptAction(msgProp.getProperty("userMenu"));
		}
	}

	/**
	 * 영화 추가
	 */
	public void insert() {
		MovieVO movieVo=new MovieVO();
		
		String title=view.getPromptStr(msgProp.getProperty("promptMovieTitle"));
		movieVo.setMovieTitle(title);
		
		String content=view.getPromptStr(msgProp.getProperty("promptMovieSummary"));
		movieVo.setMovieContent(content);
		
		LocalDate date=LocalDate.parse(view.getPromptStr(msgProp.getProperty("promptMovieDate")));
		movieVo.setMovieDate(date);
		
		boolean isFlag=movieDao.insert(movieVo);
		
		if(isFlag == false) {
			view.getPropmptMsg(msgProp.getProperty("error"));
		}else {
			view.getPromptAction(msgProp.getProperty("adminMenu"));
		}
	}

	/**
	 * 영화 삭제
	 */
	public void delete() {
		MovieVO movieVo=new MovieVO();
		
		int num=Integer.parseInt(view.getPromptStr(msgProp.getProperty("promptMovieNum")));
		movieVo.setMovieNum(num);
		
		boolean isFlag=movieDao.delete(movieVo);
		
		if(isFlag == false) {
			view.getPropmptMsg(msgProp.getProperty("error"));
		}else {
			view.getPromptAction(msgProp.getProperty("adminMenu"));
		}
	}

	/**
	 * 영화 검색
	 */
	public void search() {
		MovieVO movieVo=new MovieVO();
		
		String title=view.getPromptStr(msgProp.getProperty("promptMovieSearch"));
		movieVo.setSearchTitle(title);
		
		List<MovieVO> datas=movieDao.list(movieVo);

		if(datas == null) {
			view.getPromptAction(msgProp.getProperty("mainMenu"));
		}else {
			view.printMovieList(datas);
			view.getPromptAction(msgProp.getProperty("userMenu"));
		}
	}

	/**
	 * 프로그램 종료
	 */
	public void exit() {
		view.getPropmptMsg(msgProp.getProperty("exit"));
		System.exit(0);
	}
}
