package com.process.impl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.FilterModifWord;


/**
 * @author Administrator
 *
 */
public class SegTool_v2 {
	/*
# 1. 鍚嶈瘝  (1涓竴绫伙紝7涓簩绫伙紝5涓笁绫�)
	鍚嶈瘝鍒嗕负浠ヤ笅瀛愮被锛�
	n 鍚嶈瘝
	nr 浜哄悕
	nr1 姹夎濮撴皬
	nr2 姹夎鍚嶅瓧
	nrj 鏃ヨ浜哄悕
	nrf 闊宠瘧浜哄悕
	ns 鍦板悕
	nsf 闊宠瘧鍦板悕
	nt 鏈烘瀯鍥綋鍚�
	nz 鍏跺畠涓撳悕
	nl 鍚嶈瘝鎬ф儻鐢ㄨ
	ng 鍚嶈瘝鎬ц绱�
	nw 鏂拌瘝
# 2. 鏃堕棿璇�(1涓竴绫伙紝1涓簩绫�)
	t 鏃堕棿璇�
	tg 鏃堕棿璇嶆�ц绱�
# 3. 澶勬墍璇�(1涓竴绫�)
	s 澶勬墍璇�
# 4. 鏂逛綅璇�(1涓竴绫�)
	f 鏂逛綅璇�
# 5. 鍔ㄨ瘝(1涓竴绫伙紝9涓簩绫�)
	v 鍔ㄨ瘝
	vd 鍓姩璇�
	vn 鍚嶅姩璇�
	vshi 鍔ㄨ瘝鈥滄槸鈥�
	vyou 鍔ㄨ瘝鈥滄湁鈥�
	vf 瓒嬪悜鍔ㄨ瘝
	vx 褰㈠紡鍔ㄨ瘝
	vi 涓嶅強鐗╁姩璇嶏紙鍐呭姩璇嶏級
	vl 鍔ㄨ瘝鎬ф儻鐢ㄨ
	vg 鍔ㄨ瘝鎬ц绱�
# 6. 褰㈠璇�(1涓竴绫伙紝4涓簩绫�)
	a 褰㈠璇�
	ad 鍓舰璇�
	an 鍚嶅舰璇�
	ag 褰㈠璇嶆�ц绱�
	al 褰㈠璇嶆�ф儻鐢ㄨ
# 7. 鍖哄埆璇�(1涓竴绫伙紝2涓簩绫�)
	b 鍖哄埆璇�
	bl 鍖哄埆璇嶆�ф儻鐢ㄨ
# 8. 鐘舵�佽瘝(1涓竴绫�)
	z 鐘舵�佽瘝
# 9. 浠ｈ瘝(1涓竴绫伙紝4涓簩绫伙紝6涓笁绫�)
	r 浠ｈ瘝
	rr 浜虹О浠ｈ瘝
	rz 鎸囩ず浠ｈ瘝
	rzt 鏃堕棿鎸囩ず浠ｈ瘝
	rzs 澶勬墍鎸囩ず浠ｈ瘝
	rzv 璋撹瘝鎬ф寚绀轰唬璇�
	ry 鐤戦棶浠ｈ瘝
	ryt 鏃堕棿鐤戦棶浠ｈ瘝
	rys 澶勬墍鐤戦棶浠ｈ瘝
	ryv 璋撹瘝鎬х枒闂唬璇�
	rg 浠ｈ瘝鎬ц绱�
# 10. 鏁拌瘝(1涓竴绫伙紝1涓簩绫�)
	m 鏁拌瘝
	mq 鏁伴噺璇�
# 11. 閲忚瘝(1涓竴绫伙紝2涓簩绫�)
	q 閲忚瘝
	qv 鍔ㄩ噺璇�
	qt 鏃堕噺璇�
# 12. 鍓瘝(1涓竴绫�)
	d 鍓瘝
# 13. 浠嬭瘝(1涓竴绫伙紝2涓簩绫�)
	p 浠嬭瘝
	pba 浠嬭瘝鈥滄妸鈥�
	pbei 浠嬭瘝鈥滆鈥�
# 14. 杩炶瘝(1涓竴绫伙紝1涓簩绫�)
	c 杩炶瘝
 	cc 骞跺垪杩炶瘝
# 15. 鍔╄瘝(1涓竴绫伙紝15涓簩绫�)
	u 鍔╄瘝
	uzhe 鐫�
	ule 浜� 鍠�
	uguo 杩�
	ude1 鐨� 搴�
	ude2 鍦�
	ude3 寰�
	usuo 鎵�
	udeng 绛� 绛夌瓑 浜戜簯
	uyy 涓�鏍� 涓�鑸� 浼肩殑 鑸�
	udh 鐨勮瘽
	uls 鏉ヨ 鏉ヨ 鑰岃█ 璇存潵
	uzhi 涔�
	ulian 杩� 锛堚�滆繛灏忓鐢熼兘浼氣�濓級
# 16. 鍙硅瘝(1涓竴绫�)
	e 鍙硅瘝
# 17. 璇皵璇�(1涓竴绫�)
	y 璇皵璇�(delete yg)
# 18. 鎷熷０璇�(1涓竴绫�)
	o 鎷熷０璇�
# 19. 鍓嶇紑(1涓竴绫�)
	h 鍓嶇紑
# 20. 鍚庣紑(1涓竴绫�)
	k 鍚庣紑
# 21. 瀛楃涓�(1涓竴绫伙紝2涓簩绫�)
	x 瀛楃涓�
 	xx 闈炶绱犲瓧
 	xu 缃戝潃URL
# 22. 鏍囩偣绗﹀彿(1涓竴绫伙紝16涓簩绫�)
	w 鏍囩偣绗﹀彿
	wkz 宸︽嫭鍙凤紝鍏ㄨ锛氾紙 銆�  锛�  锝�  銆� 銆�  銆栥��   鍗婅锛�( [ { <
	wky 鍙虫嫭鍙凤紝鍏ㄨ锛氾級 銆�  锛� 锝� 銆�  銆� 銆� 銆� 鍗婅锛� ) ] { >
	wyz 宸﹀紩鍙凤紝鍏ㄨ锛氣�� 鈥� 銆� 
	wyy 鍙冲紩鍙凤紝鍏ㄨ锛氣�� 鈥� 銆�
	wj 鍙ュ彿锛屽叏瑙掞細銆�
	ww 闂彿锛屽叏瑙掞細锛� 鍗婅锛�?
	wt 鍙瑰彿锛屽叏瑙掞細锛� 鍗婅锛�!
	wd 閫楀彿锛屽叏瑙掞細锛� 鍗婅锛�,
	wf 鍒嗗彿锛屽叏瑙掞細锛� 鍗婅锛� ;
	wn 椤垮彿锛屽叏瑙掞細銆�
	wm 鍐掑彿锛屽叏瑙掞細锛� 鍗婅锛� :
	ws 鐪佺暐鍙凤紝鍏ㄨ锛氣�︹��  鈥�
	wp 鐮存姌鍙凤紝鍏ㄨ锛氣�斺��   锛嶏紞   鈥斺�旓紞   鍗婅锛�---  ----
	wb 鐧惧垎鍙峰崈鍒嗗彿锛屽叏瑙掞細锛� 鈥�   鍗婅锛�%
	wh 鍗曚綅绗﹀彿锛屽叏瑙掞細锟� 锛� 锟�  掳  鈩�  鍗婅锛�$
	 */
	private static SegTool_v2 ansj = null;

	final static String NATURE = "nature";
	final static String WORD = "word";
    //stopWordLoc表示停用词表所在的路径
	final static String stopWordLoc = "SegTool_config/StopWord(combined).txt";
	//stopWordLoc表示停用词性所在的路径，我们在分词的时候会过滤掉词性为停用词性表中的那些词
	final static String stopNatureLoc = "SegTool_config/StopNature.txt";
	final static String stkorgLoc = "SegTool_config/stkorg.dict.sz";
	public static Set<String> stkOrgSet = new HashSet<String>();
	public final static int stkorg_preDefinedFrequency = 1000;
	
	
	private SegTool_v2(){
		System.out.println("loads ansj dictionary map");
		ToAnalysis.parse("");
		System.out.println("loads stopWord and stopNature");
		InputStream stkOrgStream =this.getClass().getClassLoader().getResourceAsStream(stkorgLoc);
		loadsUserDefinedLibrary("stkorg",stkOrgStream,stkorg_preDefinedFrequency);
		InputStream stopwordLOC =this.getClass().getClassLoader().getResourceAsStream(stopWordLoc);
		System.out.println("stopword loaded from "+stopwordLOC);
		InputStream stopnatureLOC =this.getClass().getClassLoader().getResourceAsStream(stopNatureLoc);
		System.out.println("stopword loaded from "+stopnatureLOC);
		loadsStops(stopwordLOC, stopnatureLOC);
		System.out.println("finish loading");
	}
	public static SegTool_v2 getInstance(){
		if(ansj==null){
			ansj = new SegTool_v2();
		}
		return ansj;
	}

	public static void loadsUserDefinedLibrary(String type, InputStream stream, int preDefineFrequency){
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(stream,"utf-8"));
			String word = br.readLine();
			while(word!=null){
				
				UserDefineLibrary.insertWord(word, "userDefine", preDefineFrequency);
				if(type.contains("stkorg"))stkOrgSet.add(word);
				word = br.readLine();
			}
			
		}catch(Exception e){
			System.out.println("ansj loads user defined library failed");
			e.printStackTrace();
		}
	}

	/**
	 * @param stopWordLoc = in this file, each line represent one element, utf-8 encoding
	 * @param stopNatureLoc = in this file, each line represent one element
	 * @return
	 */
	public static void loadsStops(InputStream stopWordLoc, InputStream stopNatureLoc){
		
		
		try{
			if(stopWordLoc!=null & !stopWordLoc.equals("")){
				BufferedReader brStopWord = new BufferedReader(new InputStreamReader(stopWordLoc,"utf-8"));
				prepareFilter(brStopWord, WORD);
				brStopWord.close();
			}
			if(stopNatureLoc!=null & !stopNatureLoc.equals("")){
				BufferedReader brStopNature = new BufferedReader(new InputStreamReader(stopNatureLoc,"utf-8"));
				prepareFilter(brStopNature, NATURE);
				brStopNature.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void prepareFilter(BufferedReader br ,String stopWhat){
		try{
			String line = br.readLine();
			while(line!=null){

				if(stopWhat.equals(NATURE))FilterModifWord.insertStopNatures(line.trim());
				if(stopWhat.equals(WORD)){
					FilterModifWord.insertStopWord(line.trim());
				}
				line = br.readLine();
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * @param str = text
	 * @return String = segmented text with space in between
	 */
	public String segment(String str) {

		List<Term> terms = ToAnalysis.parse(str);
		terms = FilterModifWord.modifResult(terms);

		StringBuilder sb = new StringBuilder();
		//System.out.println(terms);
		for (Term term : terms) {
			if (!" ".equals(term.getName()) && !" ".equals(term.getName()) && term.getName().trim().replaceAll("[\\pP\\pM\\pS]", "").length() > 0) {
				sb.append(term.getName()).append(" ");
			}
		}
		return sb.toString();
	}

	/**
	 * @param str = text
	 * @return List<Term> = list of words with its nature
	 */
	public List<Term> ansjAnalyzer(String str) {

		List<Term> terms = ToAnalysis.parse(str);
		terms = FilterModifWord.modifResult(terms);

		return terms;
	}
	
}
