package com.rdfs.core.exeption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdfs.core.utils.RdfsUtils;
import com.rdfs.core.utils.ExceptionInfoUtil;


/**
 * <b>系统异常类</b>
 * 
 * @author xiongchun
 */
public class RdfsException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private static Logger log = LoggerFactory.getLogger(RdfsException.class);

	public RdfsException() {
		super();
	}

	/**
	 * 支持传参数给异常描述字符串进行合并
	 * 
	 * @param errID
	 * @param args
	 */
	public RdfsException(int errID, Object... args) {
		super("异常编号：" + errID);
		ExceptionVO vo = ExceptionInfoUtil.getExceptionInfo(String.valueOf(errID));
		if (RdfsUtils.isNotEmpty(vo)) {
			String errMsg = "异常编号：" + errID;
			errMsg = errMsg + "\n异常摘要：" + RdfsUtils.merge(vo.getInfo(), args);
			errMsg = errMsg + "\n异常排查建议：" + vo.getSuggest() + " 更多信息请访问开发者社区：www.osworks.cn";
			errMsg = errMsg + "\n异常详细堆栈信息";
			System.out.println(errMsg);
		} else {
			System.out.println("没有查询到异常编号为[" + errID + "]的异常配置信息。");
		}
	}

	/**
	 * 根据异常ID获取异常相关信息
	 * 
	 * @param errID
	 */
	public RdfsException(int errID) {
		super("异常编号：" + errID);
		ExceptionVO vo = ExceptionInfoUtil.getExceptionInfo(String.valueOf(errID));
		if (RdfsUtils.isNotEmpty(vo)) {
			String errMsg = "异常编号：" + errID;
			errMsg = errMsg + "\n异常摘要：" + RdfsUtils.merge(vo.getInfo(), "");
			errMsg = errMsg + "\n异常排查建议：" + vo.getSuggest() + " 更多信息请访问开发者社区：www.osworks.cn";
			errMsg = errMsg + "\n异常详细堆栈信息";
			System.out.println(errMsg);
		} else {
			System.out.println("没有查询到异常编号为[" + errID + "]的异常配置信息。");
		}
	}
	
	/**
	 * 直接打印简单信息
	 * 
	 * @param pMsg
	 * @param pNestedException
	 */
	public RdfsException(String pMsg) {
		super(pMsg);
		log.error(pMsg);
	}

	/**
	 * 直接打印简单信息和异常堆栈
	 * 
	 * @param pMsg
	 * @param pNestedException
	 */
	public RdfsException(String pMsg, Throwable pNestedException) {
		super(pMsg);
		pNestedException.printStackTrace();
	}
	
	/**
	 * 直接打印异常堆栈
	 * 
	 * @param pMsg
	 * @param pNestedException
	 */
	public RdfsException(Throwable pNestedException) {
		super(pNestedException);
		pNestedException.printStackTrace();
	}

}
