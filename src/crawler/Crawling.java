package crawler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import model.dao.MovieDAO;
import model.vo.MovieVO;
import util.FileLoaderUtil;

/**
 * CGV 영화를 크롤링해서 1위부터 15위까지 영화 제목, 개봉일, 줄거리를
 * 가져와서 데이터베이스에 저장하는 클래ㅅ,
 * @author indi
 *
 */
public class Crawling {
	private static Properties crawlerTagProp=FileLoaderUtil.getInstance().getCrawlerTagProperties();
	
	
	/**
	 * CGV 영화 순위 1위부터 15위까지 크롤링
	 * @return urlDatas
	 */
	public static List<String> urls() {
		String urlRoot = "http://www.cgv.co.kr/";
		String url = urlRoot + "movies/?lt=1&ft=0";

		List<String> urlDatas = new ArrayList<String>();

		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		Elements eles = doc.select(crawlerTagProp.getProperty("movieTopList"));
		System.out.println("eles: "+eles);
		for (int i = 0; i < 15; i++) {
			urlDatas.add(urlRoot + eles.get(i).getElementsByAttribute("href").attr("href"));
		}
		return urlDatas;
	}
	
	/**
	 * CGV 영화 흥행 순위 크롤링(영화 제목, 영화 개봉일, 영화 줄거리)
	 * 크롤링한 결과를 movieDao.insert에 저장
	 * @param movieDao
	 */
	public static void crawling(MovieDAO movieDao) {
		List<String> url = urls();
		for (int i = 0; i < url.size(); i++) {
			Document doc = null;
			try {
				doc = Jsoup.connect(url.get(i)).get();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Elements eles1 = doc.select(crawlerTagProp.getProperty("movieTitle"));
			Iterator<Element> itr1 = eles1.iterator();
			Elements eles2 = doc.select(crawlerTagProp.getProperty("movieDate"));
			Element secondeles = eles2.get(2);
			Iterator<Element> itr2 = new Elements(secondeles).iterator();
			Elements eles3 = doc.select(crawlerTagProp.getProperty("movieSummary"));
			Iterator<Element> itr3 = eles3.iterator();
			if(itr1.hasNext() && itr2.hasNext() && itr3.hasNext()) {
				MovieVO movieVo = new MovieVO();
				String title = itr1.next().text();
				movieVo.setMovieTitle(title);
				String date1 = itr2.next().text();
				int year = Integer.parseInt(date1.split("\\.")[0]); // 연도: 2022
				int month = Integer.parseInt(date1.split("\\.")[1]); // 월: 11
				int day = Integer.parseInt(date1.split("\\.")[2]); // 일: 22
				LocalDate date = LocalDate.of(year, month, day);
				movieVo.setMovieDate(date);
				String contnet = itr3.next().text();
				movieVo.setMovieContent(contnet);
				List<MovieVO> datas=movieDao.list(null);
				if(datas == null) {
					movieDao.insert(movieVo);
				}else {
					return;
				}
			}
		}
	}
}
