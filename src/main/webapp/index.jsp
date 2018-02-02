<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	
	<h4>header和cookie</h4>
	<c:set var="kv" value="${fn:split(header, ',')}" />
	<c:forEach var ="k" items="${kv}">
	<c:out value="${k}"/><p>
	</c:forEach>
	
	<h4>保存在map集合中的请求头（使用jstl循环输出）：</h4>
 	<c:forEach var ="i" items="${map}">
    <c:out value="${i.key}"/> : <c:out value="${i.value}"/><p>
	</c:forEach>
	<h4>保存在list集合中的cookie（使用jstl循环输出）:</h4>
	<c:forEach var ="j" items="${list}">
    <c:out value="${j}"/><p>
	</c:forEach>
	<h4>数字格式化：</h4>
	<c:set var="balance" value="1000009.0000" />
	 <fmt:formatNumber type="number"  maxIntegerDigits="9" minFractionDigits="2"
	  value="${balance}"/></p>
	 <h4>日期格式化：</h4>
     <c:set var="now" value="<%=new java.util.Date()%>" />
     <fmt:formatDate pattern="yyyy年MM月dd日" value="${now}" /></p>
     <fmt:formatDate pattern="E" value="${now}" /></p>
	<fmt:formatDate type="date" value="${now}" /></p>
     <fmt:formatDate pattern="HH:mm" value="${now}" /></p>
</body>
</html>