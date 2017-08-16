package com.yanbao.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yanbao.core.page.Page;
import com.mall.model.User;

@Repository
public interface UserMapper {

	User getById(@Param("id") String id);

	User getByCondition(@Param("model") User model);

	Integer count(@Param("model") User condition);

	List<User> getList(@Param("model") User condition, @Param("page") Page page);

	Integer add(@Param("model") User model);

	Integer update(@Param("id") String id, @Param("model") User model);

	Integer updateBySignIn(@Param("id") String id, @Param("model") User model);

	Integer updateScore(@Param("id") String id, @Param("score") Double score);

	Integer updateDoudou(@Param("id") String id, @Param("doudou") Double doudou, @Param("douSignTime") Date douSignTime);

	Integer updateEp(@Param("id") String id, @Param("exchangeEP") Double exchangeEP);

	Integer updateGroupChildCountA(@Param("id") String id);

	Integer updateGroupChildCountB(@Param("id") String id);

	Integer updateGroupChildCountC(@Param("id") String id);
	
	Integer updateGroupChildCount(@Param("id") String id,@Param("A") Integer A,@Param("B") Integer B ,@Param("C") Integer C  );
	
	Integer updateRegistrationId(@Param("id") String id, @Param("registrationId") String registrationId);

	Integer updateEpById(@Param("id") String id, @Param("ep") Double ep) throws Exception;

	Integer updateAllPerformance(@Param("id") String id, @Param("performanceOne") Double performanceOne, @Param("performanceTwo") Double performanceTwo, @Param("performanceThree") Double performanceThree);

	Integer updatePerformanceOne(@Param("id") String id, @Param("count") Double count);

	Integer updatePerformanceTwo(@Param("id") String id, @Param("count") Double count);

	Integer updatePerformanceThree(@Param("id") String id, @Param("count") Double count);

	Integer updateConsumeEPById(@Param("id") String id, @Param("consumeEP") Double consumeEP) throws Exception;

	String getMinGroupType(@Param("firstreferrer") String firstreferrer);

	List<User> getInnerList(@Param("uidList") List<Integer> uidList);

	Integer updateBindEpById(@Param("id") String id, @Param("ep") Double ep) throws Exception;

	Integer updateBindDoudouById(@Param("id") String id, @Param("doudou") Double doudou) throws Exception;
	
	List<User> getUpdateList(@Param("page") Page page)  throws Exception;
	
	List<User> getLeafList(@Param("page") Page page) throws Exception;

	Integer batchUpdatePerformanceOne(@Param("list") List<String> listA, @Param("epCount") Double epCount);

	Integer batchUpdatePerformanceTwo(@Param("list") List<String> listB, @Param("epCount") Double epCount);

	Integer batchUpdatePerformanceThree(@Param("list") List<String> listC,@Param("epCount")  Double epCount);
}
