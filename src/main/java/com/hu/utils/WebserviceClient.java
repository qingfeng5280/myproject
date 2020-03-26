package com.hu.utils;

import org.apache.axis.Constants;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author 胡晶晶
 * axis.jar，commons-discovery.jar，commons-logging-1.0.4.jar，jaxrpc.jar，wsdl4j.jar，mail.jar。
 */
public class WebserviceClient {


	public static void main(String[] args) throws IOException {

		String ticketTz = "331528e024344581afb6e6b68d3f6911";
		String projid = "924926dc720143adb4dda97f8a74adbe";
		String appkey = "bdd6d64a821e4277888594df9a3646a9";
		String secretKey = "9a70f4b94178444f9aedbd086b921b21";

		String requiretime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

		String sign = appkey + secretKey + requiretime;
		String token = MD5Util.MD5Encode(sign);

		net.sf.json.JSONObject base = new net.sf.json.JSONObject();
		base.put("ticketTz", ticketTz);
		base.put("requestTime", requiretime);
		base.put("token", token);
		System.out.println(base.toString());

		try {
			String endpoint = "http://172.16.80.132:8888/yth/ws/userService?wsdl";
			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(endpoint);
			call.setOperationName(new QName("http://service.webservices.modules.zjtzsw.com", "getUserInfo"));
			call.addParameter("jsonObj", Constants.XSD_STRING, ParameterMode.IN);
			call.setReturnType(Constants.XSD_STRING);// 接口的参数
			String result = (String) call.invoke(new Object[] { base.toString() });
			// 给方法传递参数，并且调用方法
			System.out.println("result is " + result);
		} catch (Exception e) {
			System.err.println(e.toString());
		}


	}

	public String  getBanjInfo(String ticket, String projid) {

		String result = "" ;
		String appkey = "bdd6d64a821e4277888594df9a3646a9";
		String secretKey = "9a70f4b94178444f9aedbd086b921b21";
		String requiretime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String sign = appkey + secretKey + requiretime;
		String token = MD5Util.MD5Encode(sign);

		net.sf.json.JSONObject base = new net.sf.json.JSONObject();
		base.put("ticket", ticket);
		base.put("projId", projid);
		base.put("requestTime", requiretime);
		base.put("token", token);
		//System.out.println(base.toString());

		try {
			String endpoint = "http://172.16.80.132:8888/yth/ws/banjxxService?wsdl";
			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(endpoint);
			call.setOperationName(new QName("http://service.webservices.modules.zjtzsw.com", "getBanjInfo"));
			call.addParameter("jsonObj", Constants.XSD_STRING, ParameterMode.IN);
			call.setReturnType(Constants.XSD_STRING);// 接口的参数
			result = (String) call.invoke(new Object[] { base.toString() });
			// 给方法传递参数，并且调用方法
			//System.out.println("result is " + result);
		} catch (Exception e) {
			System.err.println(e.toString());
			result = "一体化接口调用异常" ;
		}

		return result;
	}


	public String  getUserInfo(String ticket) {

		String result = "" ;
		String appkey = "bdd6d64a821e4277888594df9a3646a9";
		String secretKey = "9a70f4b94178444f9aedbd086b921b21";
		String requiretime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String sign = appkey + secretKey + requiretime;
		String token = MD5Util.MD5Encode(sign);

		net.sf.json.JSONObject base = new net.sf.json.JSONObject();
		base.put("ticket", ticket);
		base.put("requestTime", requiretime);
		base.put("token", token);
		System.out.println(base.toString());

		try {
			String endpoint = "http://172.16.80.132:8888/yth/ws/userService?wsdl";
			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(endpoint);
			call.setOperationName(new QName("http://service.webservices.modules.zjtzsw.com", "getUserInfo"));
			call.addParameter("jsonObj", Constants.XSD_STRING, ParameterMode.IN);
			call.setReturnType(Constants.XSD_STRING);// 接口的参数
			result = (String) call.invoke(new Object[] { base.toString() });
			// 给方法传递参数，并且调用方法
			System.out.println("result is " + result);
		} catch (Exception e) {
			System.err.println(e.toString());
			result = "一体化接口调用异常" ;
		}

		return result;
	}

}
