<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
    PUBLIC "-//ibatis.apache.org//DTD Mapper 3.0//EN"
    "http://ibatis.apache.org/dtd/ibatis-3-mapper.dtd">

<mapper namespace="${namespace}">
<#list originalSqls as sql>
    <select id="${sql.sqlId}_count" parameterType="hashmap" resultType="int">
        select count(*) from (
            ${sql.sqlContent}
            limit ${MAX_COUNT} 
        ) tmp
    </select>
</#list>
</mapper>