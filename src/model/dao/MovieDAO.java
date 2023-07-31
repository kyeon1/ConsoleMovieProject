package model.dao;

import java.util.List;

import model.vo.MovieVO;

/**
 * MovieDAO 클래스
 * @author indi
 *
 */
public class MovieDAO extends CommonDAO{
	
	/**
	 *MovieDAO 생성자
	 */
	public MovieDAO() {
		
	}

	/**
	 * 영화 추가
	 * @param movieVo
	 * @return boolean
	 */
	public boolean insert(MovieVO movieVo) {
		return executeUpdate("insertMovie", movieVo.getMovieTitle(), movieVo.getMovieContent(), movieVo.getMovieDate());
	}
	
	/**
	 * 영화 삭제
	 * @param movieVo
	 * @return boolean
	 */
	public boolean delete(MovieVO movieVo) {
		return executeUpdate("deleteMovie", movieVo.getMovieNum());
	}

	/**
	 * 영화 상세
	 * @param movieVo
	 * @return List<MovieVO>
	 */
	public List<MovieVO> select(MovieVO movieVo){
		return executeQuery("selectMovie", MovieVO.class, movieVo.getMovieNum());
	}

	/**
	 * 영화 목록
	 * @param movieVo
	 * @return List<MovieVO>
	 */
	public List<MovieVO> list(MovieVO movieVo){
//		String query=loadSqlMap().get("listMovie");
		if(movieVo==null) {
			return executeQuery("listMovie",MovieVO.class);			
		}else if(movieVo.getSearchTitle()!=null) {
			return executeQuery("searchMovie", MovieVO.class, movieVo.getSearchTitle());
		}
		return null;
	}
}
