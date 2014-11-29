package com.larcloud.component;

import com.larcloud.util.ParamMap;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-5-7
 * Time: 下午5:22
 * To change this template use File | Settings | File Templates.
 */
public interface IInstructionComponent {
    public void addAndSend(ParamMap params);
    public Boolean canSendToTerminal(ParamMap params);
}
