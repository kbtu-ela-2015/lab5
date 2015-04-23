/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dauren
 */
public class SenderFactory {
    public static SenderFactory instance = new SenderFactory();
    public SenderUtil senderUtil;
    
    private SenderFactory(){
        
    }
    
    public  static SenderFactory getInstance(){
        return instance;
    }
    
    public SenderUtil getSenderUtil(){
        if(senderUtil==null) senderUtil = new SenderUtil();
            return senderUtil;
    }
}
