package com.lx.framework.tool.startup.service.impl;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.lx.framework.tool.redis.util.RedisConstant;
import com.lx.framework.tool.redis.util.RedisUtil;
import com.lx.framework.tool.startup.handler.customException.BusinessException;
import com.lx.framework.tool.startup.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;


/**
 * Description: 幂等性token 生产
 * Date: 2022/4/20 16:11
 * @author: xin.liu
 * @version 1.0.0
 */
@Service
public class TokenServiceImpl implements TokenService {



    /**
     * Description 创建token
     * @return java.lang.String
     * @author xin.liu
     * @date 2022/4/20 16:12
     */
    @Override
    public String createToken() {
        String str = IdUtil.randomUUID();
        StrBuilder token = new StrBuilder();
        try {
            token.append(RedisConstant.TOKEN_PREFIX).append(str);
            RedisUtil.set(token.toString(), token.toString(), 1000L);
            boolean notEmpty = StrUtil.isNotEmpty(token.toString());
            if (notEmpty) {
                return token.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }



    /**
     * Description 检验token
     * @param request
     * @return boolean
     * @author xin.liu
     * @date 2022/4/20 16:13
     */
    @Override
    public boolean checkToken(HttpServletRequest request) {
        String token = request.getHeader(RedisConstant.TOKEN_NAME);
        if (StrUtil.isBlank(token)) {
            // header中不存在token
            token = request.getParameter(RedisConstant.TOKEN_NAME);
            if (StrUtil.isBlank(token)) {
                // parameter中也不存在token
                throw new BusinessException("token不存在");
            }
        }
        if (!RedisUtil.hasKey(token)) {
            throw new BusinessException("token无效");
        }
        RedisUtil.del(token);
        return true;
    }
}