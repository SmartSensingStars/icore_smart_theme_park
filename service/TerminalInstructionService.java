package com.larcloud.service;

import com.larcloud.model.TerminalInstruction;
import com.larcloud.util.ParamMap;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-3-2
 * Time: 上午11:44
 * To change this template use File | Settings | File Templates.
 */
public interface TerminalInstructionService extends BaseService<TerminalInstruction> {
    public Boolean canSendToTerminal(ParamMap params);
}
