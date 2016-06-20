package com.process.impl;

import com.process.impl.SegTool_v2;
import com.process.impl.MessageClassify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BinaryClassify implements com.process.service.BinaryClassify {
	static String modelname="LibSVM.model";   
	
	@Override
	public double BinaryClassify(String title, String content) {
		double predicted=0;
		try
        {
            MessageClassify messageCl=null;
            messageCl=messageCl.loadModel(modelname);
             
            try
            {
            	ObjectOutputStream modelOutObjectFile = new ObjectOutputStream(new FileOutputStream(modelname));
                modelOutObjectFile.writeObject(messageCl);
                modelOutObjectFile.close();
                } catch (Exception e){
                	
                }
            String news=title+"\r\n"+content;
		    String newsbyseg=SegTool_v2.getInstance().segment(news);
            predicted=messageCl.classifyMessage(newsbyseg);
        }catch(Exception e){
        	
        }
		return predicted;
	}

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();        
		BinaryClassify BC=new BinaryClassify();
//		String title="鄂尔多斯：关于召开2016年第二次临时股东大会的通知";
//		String content="内蒙古鄂尔多斯资源股份有限公司董事会决定于2016年4月27日10点00分召开2016年第二次临时股东大会，本次股东大会所采用的表决方式是现场投票和网络投票相结合的方式，采用上海证券交易所网络投票系统，通过交易系统投票平台的投票时间为股东大会召开当日的交易时间段，即9：15-9：25，9：30-11：30，13：00-15：00；通过互联网投票平台的投票时间为股东大会召开当日的9：15-15：00，审议关于为下属子公司提供贷款担保的议案、关于收购内蒙古鄂尔多斯能源有限责任公司的全部股权的议案。 仅供参考，请查阅当日公告全文。";
//		String title="海航创新：释放劲爆信号，近期有望成为暴涨妖王！";
//		String content="海航创新(600555)变更名称及经营范围 　　2016年1月10日晚间公告，为顺应公司战略转型及未来发展需要，董事会同意将公司中文名称由“上海九龙山旅游股份有限公司”变更为“海航创新（上海）股份有限公司”；A股证券简称变更为“海航创新”，B股证券简称变更为“海航创新 B”，A、B股证券代码不变。同时为顺应公司战略转型及未来发展需要，同意将公司经营范围由“旅游景点综合经营管理，酒店管理，游艇销售、展览；受所投资企业委托，为其提供投资经营决策，营销策划，会展、会务服务，企业管理咨询。”变更为“旅游投资（禁止外商投资的除外）及管理；旅游景点综合经营管理，酒店管理；游艇销售、展览；受所投资企业委托，为其提供投资经营决策，营销策划，会展、会务服务，企业管理咨询；商务信息咨询；体育活动组织策划；节能服务；软件开发，信息技术咨询服务。”（具体内容以工商部门核准的内容为准）。授权公司管理层办理公司经营范围变更相关手续。 　　 　　走势点评：近期的平均成本为6.38元，股价在成本下方运行。空头行情中，目前正处于反弹阶段。 　　海航创新(600555)操作思路、买卖点位，可关注微信公众号：雷旺域股评（w600007），能够获得最精准的投资情报。";
//		File testdata=new File("traindatabyseg");
//		for(File dir:testdata.listFiles()){
//			for(File file:dir.listFiles()){
//				
//			}
//		}
		String title="“小冷”机种蕴含“大热”蓝海";
//		String content="3月27日，北京湖南企业商会（原北京湖南企业协会）第三届理事会第二次会议在北京湖南大厦湖南厅召开，东方雨虹旗下品牌洛迪硅藻泥受邀参加本次会议。  　　此次会议以“凝聚湘商力量，共谋商会发展”为主题，旨在促进各行业及企业间的交流与合作，促进湘商共同发展，积极构建和谐社会。汇聚了各大湘籍优秀会员企业和代表，并邀请了熟悉湖南省情、关心湖南发展的特约顾问和嘉宾，共商发展大计，集中资金和项目力量，帮助湖南企业成长、发展和壮大。?? 与会企业家合照 ??长沙洛迪环保科技有限公司本次亮相协会，凭借在行业内所取得的成就，不断上升的发展趋势以及巨大的发展潜力引起了广泛关注，公司的努力得到了以湖南企业商会会长李卫国先生为首的各商会领导的认可。 湖南企业商会会长李卫国先生做分享 ?　会上，洛迪总经理吴浩也作为优秀企业家代表受到协会表彰并接受颁奖。?? 国务院国有资产监督管理委员会经济研究中心副主任彭建国为长沙洛迪总经理吴浩（左四）颁发证书 ?? 　　一方面，作为一家集研发、设计、生产、销售于一体的专业环保型墙面材料公司，洛迪公司始终秉承“让亿万家庭健康生活”的企业使命，将健康福音带入千家万户，为环保事业的推进和产业结构性调整做出了杰出贡献。  　　另一方面，洛迪通过践行科学管理、积极创新、勇于开拓的发展道路，企业规模不断扩大，并与中国室内装饰协会、保利地产、苏州金螳螂建筑装饰股份有限公司、中南大学等建立了良好的战略合作关系。  目前洛迪的产品广泛应用于各大工程项目和地产精装房项目中，洛迪与全国性地产企业及高端设计界、顶尖科研院校联手合作，正在开启硅藻泥行业的新时代。";
		File file=new File("D:\\testdata\\badcase3.txt");
		InputStreamReader read = new InputStreamReader(new FileInputStream(file));
        BufferedReader bufferedReader = new BufferedReader(read);
        String content=null;
        String lineTxt = null;
        while((lineTxt = bufferedReader.readLine()) != null){
        	content=content+lineTxt;
        	}
         read.close();
		double predicted=BC.BinaryClassify(title, content);
		System.out.println("Message classified as : "+(int) predicted);
		long end = System.currentTimeMillis(); 
		System.out.println("运行时间："+(end-start)+"毫秒");   
//		System.out.println();
		
	}
}
