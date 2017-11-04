<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<br>
<ul>
<c:forEach  items="${mealWithExceeds}" var="meal">
    <c:if test="${meal.isExceed()==true}"> <li style="color:red"> ${meal.dateTime.toLocalDate()}      ${meal.description}       ${meal.calories}</li><br></c:if>
    <c:if test="${meal.isExceed()==false}"> <li style="color:green"> ${meal.dateTime.toLocalDate()}       ${meal.description}       ${meal.calories}</li><br></c:if>
</c:forEach>
</ul>

</body>
</html>
