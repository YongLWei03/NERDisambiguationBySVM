package com.process.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;


public class MessageClassify implements Serializable {
//	private static final long serialVersionUID = 1L;
	private Instances instances = null; 
    private StringToWordVector filter = new StringToWordVector(); 
    private Classifier classifier = new LibSVM();
    static String modelname="LibSVM3.model"; 
    public static void main(String[] options)
    {
    	
        try
        {
            MessageClassify messageCl=null;
            if(new File(modelname).exists())
                messageCl=loadModel(modelname);
            else
            {   SegmentTraindata("organ","organbyseg");
                messageCl=trainModel();
                try
                {
                    ObjectOutputStream modelOutObjectFile = new ObjectOutputStream(new FileOutputStream(modelname));
                    modelOutObjectFile.writeObject(messageCl);
                    modelOutObjectFile.close();
                } catch (Exception e){}
            }
//            int sum=0;
            List<Integer> sum=new ArrayList<Integer>();
            File testdata=new File("organbyseg");
    		for(File dir:testdata.listFiles()){
    			System.out.println(dir.getName());
    			int loop=0;
    			for(File file:dir.listFiles()){
    				String news=messageCl.getStringFromFile(file);
    				String newsbyseg=SegTool_v2.getInstance().segment(news);
    				double predicted=messageCl.classifyMessage(newsbyseg);
//    				System.out.println(String.valueOf((int)predicted));
//    				System.out.println(dir.getName());
    				if(String.valueOf((int)predicted).equals(dir.getName())){
    					loop=loop+1;
    					
    				}
    				else{
    					System.out.println(file.getName());
    				}
    			}
    			sum.add(loop);
    		}
    		System.out.println(sum);
    		
//            String title="鄂尔多斯：关于召开2016年第二次临时股东大会的通知";
//            String content="内蒙古鄂尔多斯资源股份有限公司董事会决定于2016年4月27日10点00分召开2016年第二次临时股东大会，本次股东大会所采用的表决方式是现场投票和网络投票相结合的方式，采用上海证券交易所网络投票系统，通过交易系统投票平台的投票时间为股东大会召开当日的交易时间段，即9：15-9：25，9：30-11：30，13：00-15：00；通过互联网投票平台的投票时间为股东大会召开当日的9：15-15：00，审议关于为下属子公司提供贷款担保的议案、关于收购内蒙古鄂尔多斯能源有限责任公司的全部股权的议案。 仅供参考，请查阅当日公告全文。";
//    		String news=title+"\r\n"+content;
//            String newsbyseg=SegTool_v2.getInstance().segment(news);
//            messageCl.classifyMessage(newsbyseg);
                  
        } 
        catch (Exception e){
        	e.printStackTrace();
        }
    }
    //将原始训练数据中的每一篇文档分词。训练数据保存方式，每一类数据放在一个文件夹下，两类数据再放到一个大的文件夹下。
    public static void SegmentTraindata(String sdir,String ddir) throws Exception{
		File DFolder=new File(ddir);
		DFolder.mkdir();
		String SFolder="./"+sdir;
		File traindata=new File(SFolder);
		File[] files=traindata.listFiles();
		for(int i=0;i<files.length;i++){
			if (files[i].isDirectory()){
				File DFolderChildren=new File(ddir+"//"+files[i].getName());
				DFolderChildren.mkdir();
				File[] filess=files[i].listFiles();
				for(int j=0;j<filess.length;j++){
					InputStreamReader read = new InputStreamReader(new FileInputStream(filess[j]));
		            BufferedReader bufferedReader = new BufferedReader(read);
		            String text=null;
		            String lineTxt = null;
		            while((lineTxt = bufferedReader.readLine()) != null){
		            	text=text+lineTxt;
		            	}
		             read.close();
		             String textseg=SegTool_v2.getInstance().segment(text);
		             FileWriter fw = new FileWriter( new File(ddir+"//"+files[i].getName()+"//"+filess[j].getName()) );
		  	         BufferedWriter bw = new BufferedWriter(fw);
		  	         bw.write( textseg);
		  	         bw.close();
		  	         fw.close();
				}
			}
		}
	}
    
 
     
    /**
     * 构造分类器，主要及时对数据格式，类标，类别数目等进行说明
     */
    public MessageClassify() throws Exception
    {
        String nameOfDataset = "MessageClassification";     
        FastVector attributes = new FastVector(2);
        attributes.addElement(new Attribute("Message", (FastVector) null));
        FastVector classValues = new FastVector(2);//类标向量，共有两类
        classValues.addElement("0");
        classValues.addElement("1");
        attributes.addElement(new Attribute("Class", classValues));             
        instances = new Instances(nameOfDataset, attributes, 100);//可以把instance认为是行，attribute认为是列
        instances.setClassIndex(instances.numAttributes() - 1);//类表在instance中的那列
    }
 
    /**
     * 添加数据到训练集中
     */
    public void updateData(String message, String classValue) throws Exception
    {
        Instance instance = makeInstance(message, instances);
        instance.setClassValue(classValue);
        instances.add(instance);    
    }
     
    /**
     * 文本分类要特别一点，因为在使用StringToWordVector对象计算文本中词项(attribute)权重的时候需要用到全局变量，比如DF，所以这里需要批量处理
     * 在weka中要注意有些机器学习算法是批处理有些不是
     */
    public void finishBatch() throws Exception
    {
    	filter.setIDFTransform(true);
        filter.setInputFormat(instances);
        Instances filteredData = Filter.useFilter(instances, filter);//这才真正产生符合weka算法输入格式的数据集
        classifier.buildClassifier(filteredData);//真正的训练分类器
    }
     
    /**
     * 分类过程
     */
    public double classifyMessage(String message) throws Exception
    
    {          
        filter.input(makeInstance(message, instances.stringFreeStructure()));
        Instance filteredInstance = filter.output();//必须使用原来的filter
         
        double predicted = classifier.classifyInstance(filteredInstance);//(int)predicted是类标索引
//        System.out.println("Message classified as : "
//                + instances.classAttribute().value((int) predicted));
        return predicted;
    }
 
// 将一个文本数据转换成weka可以识别的Instance
    private Instance makeInstance(String text, Instances data)
    {       
        Instance instance = new Instance(2);        
        Attribute messageAtt = data.attribute("Message"); 
        instance.setValue(messageAtt, messageAtt.addStringValue(text));     
        instance.setDataset(data);
        return instance;
    }
// 读取文件内容到一个字符串里
    public static String getStringFromFile(File file)
    {
        StringBuilder sb=new StringBuilder();
        try
        {
            BufferedReader br=new BufferedReader(new FileReader(file));
            String line;
            while(true)
            {               
                if((line=br.readLine())==null) break;
                sb.append(line.trim());
            }       
            br.close();
        } catch (Exception e){
        	e.printStackTrace();
        }
        return sb.toString();
    }
     
     
    
    /**
     * 训练分类器
     */
    private static MessageClassify trainModel()
    {
        MessageClassify mc=null;
        try
        {
            mc = new MessageClassify(); 
            String basePath="organbyseg";
//            String basePath="traindata";
            File base=new File(basePath);
            File[] files=base.listFiles();
//            System.out.println(files.length);
            for(int i=0;i<files.length;i++)
            {
            	if(files[i].isDirectory()){
            		File[] filess=files[i].listFiles();
//            		System.out.println(filess.length);
            		for(int j=0;j<filess.length;j++)
            		{
            			
//            			System.out.println(filess[j].getName());
                        String message=getStringFromFile(filess[j]);
                        String classValue=files[i].getName();
                        mc.updateData(message, classValue);//添加一条训练样本，classvalue就是类标
                        
            		}               
            }
            }
            mc.finishBatch();//训练过程
             
        } catch (Exception e){
        	e.printStackTrace();
        }
        return mc;
    }
// 下载分类器
    public static MessageClassify loadModel(String modelname)
    {
        MessageClassify mc=null;
        try
        {
            ObjectInputStream modelInObjectFile =new ObjectInputStream(new FileInputStream(modelname));
            mc = (MessageClassify) modelInObjectFile.readObject();
            modelInObjectFile.close();
        }
        catch (Exception e){
        	e.printStackTrace();
        }
        return mc;      
    }
    
}
