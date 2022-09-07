package cc.pkrs.livecc;

import com.alibaba.fastjson.JSON;

import cc.pkrs.livecc.entity.RespMsgDTO;

public class test {
    private void testFi() {
        RespMsgDTO respMsg = JSON.parseObject("123", RespMsgDTO.class);
    }
}
