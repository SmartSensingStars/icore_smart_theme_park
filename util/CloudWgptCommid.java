package com.larcloud.util;

/**
 * �����������ƶ���ƽ̨��ָ��Ķ���
 * 
 * @author jnad
 * 
 */
public class CloudWgptCommid {

	/**
	 * ��ͨ�ŵ�������
	 */
	public static final byte COMMUNICATE = (byte) 0x00;

	/**
	 * ֪ͨ�ն��Ѿ�����(����)
	 */

	public static final byte OFFLINE = (byte) 0x13;
	/**
	 * ֪ͨ�ն��Ѿ�����(����)��ACKָ��
	 */

	public static final byte OFFLINE_ACK = (byte) 0x83;
	
	
	/**
	 * ƽ̨����ǿ���ն�����ָ��
	 */
	public static final byte FORCED_OFFLINE  = (byte) 0x12;
	
	/**
	 * ƽ̨����ǿ���ն�����ָ���ACKָ��
	 */
	
	public static final byte FORCED_OFFLINE_ACK  = (byte) 0x82;

	/**
	 * ���ص�ǰ�ƶ������ն����
	 */
	public static final byte NWLIST  = (byte) 0x11;
	/**
	 * ���ص�ǰ�ƶ������ն�����ACKָ��
	 */
	public static final byte NWLIST_ACK  = (byte) 0x11;
}
