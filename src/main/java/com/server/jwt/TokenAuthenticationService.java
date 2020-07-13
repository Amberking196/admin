package com.server.jwt;
 
import java.util.Date; 
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;  
import org.springframework.stereotype.Service;

import com.server.filter.WebFilter;

@Service(value="TokenAuthenticationService")
public class TokenAuthenticationService { 

	public static Logger log = LogManager.getLogger( TokenAuthenticationService.class);
	static final long EXPIRATIONTIME = 5*60*60*1000;     // 3小时
    static final String SECRET = "P@ssw02d";            // JWT密码
    static final String TOKEN_PREFIX = "Bearer";        // Token前缀
    static final String HEADER_STRING = "Authorization";// 存放Token的Header Key
    
    static final String SECRETT = "defaultSecret";
 
    // 生成token
    public String generateToken(Map<String, Object> claims) {
       
        String JWT = Jwts.builder()
                // 保存权限（角色）
                .claim("authorities", "ROLE_ADMIN,AUTH_WRITE")
                // 用户名写入标题
                .setSubject(claims.get("userId").toString())
                // 有效期设置
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                // 签名设置
                        .signWith(SignatureAlgorithm.HS512, SECRET)
                        .compact();
    	return  JWT;
    }
    
    public String generateToken(String userId) {
        
        String JWT = Jwts.builder()
                // 保存权限（角色）
                .claim("authorities", "ROLE_ADMIN,AUTH_USER")
                // 用户名写入标题
                .setSubject(userId)
                // 有效期设置
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                // 签名设置
                        .signWith(SignatureAlgorithm.HS512, SECRET)
                        .compact();
    	return  JWT;
    }
    
   //分解token
   public static Claims getClaimsFromToken(String token) { 
    	Claims claims; 
    	try { 
    		claims = Jwts.parser() .setSigningKey(SECRET) 
    				.parseClaimsJws(token) .getBody(); 
    		} catch (Exception e){ 
    			claims = null; 
    			} 
    	return claims;
    }
   
   public boolean checkToken(String token)  { 
		log.info("TokenAuthenticationService---------checkToken------ start"); 
	   Claims claims=getClaimsFromToken(token); 
	   boolean reStr=false;
	   if(claims==null){
		   reStr=false; 
	   }else{    
		        long tokenDate=claims.getExpiration().getTime(); 
		        long current=System.currentTimeMillis();
		        if(tokenDate<current){
		               reStr=false;
		         }else{  
		            	reStr=true;
                 }
		   } 
		log.info("TokenAuthenticationService---------checkToken------ end"); 
	   return reStr;
   }
   
   public String getIdByToken(String token){
	   Claims claims=getClaimsFromToken(token); 
	   String id=claims.getSubject();
	   return id;
   }
   
   /**
    * <pre>
    *  验证token是否失效
    *  true:过期   false:没过期
    * </pre>
    */
   public Boolean isTokenExpired(String token) {
       try {
           final Date expiration = getExpirationDateFromToken(token);
           return expiration.before(new Date());
       } catch (ExpiredJwtException expiredJwtException) {
           return true;
       }
   }
   
   /**
    * 获取jwt失效时间
    */
   public Date getExpirationDateFromToken(String token) {
       return getClaimFromToken(token).getExpiration();
   }
   
   /**
    * 获取jwt的payload部分
    */
   public Claims getClaimFromToken(String token) {
       return Jwts.parser()
               .setSigningKey(SECRETT)
               .parseClaimsJws(token)
               .getBody();
   }
}
