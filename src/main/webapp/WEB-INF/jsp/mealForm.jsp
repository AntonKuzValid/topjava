<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <jsp:include page="fragments/headTag.jsp"/>
</head>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<section>
    <h3><a href="../../"><spring:message code="common.home"/></a></h3>
    <h2><spring:message code="${meal.id == null ?'meal.addMeal':'meal.updateMeal'}"/></h2>
    <hr>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <form:form method="post" action="/meals/mealForm" modelAttribute="meal">
        <form:input type="hidden" path="id" value="${meal.id}"/>
        <dl>
            <dt><spring:message code="meal.dateTime"/>:</dt>
            <dd><form:input type="datetime-local" value="${meal.dateTime}" path="dateTime" required="required"/></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.description"/>:</dt>
            <dd><form:input type="text" value="${meal.description}" path="description" size="40" required="required"/></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.calories"/>:</dt>
            <dd><form:input type="number" value="${meal.calories}" path="calories" required="required"/></dd>
        </dl>
        <button type="submit"><spring:message code="meal.saveMeal"/></button>
        <button onclick="window.history.back()" type="button"><spring:message code="common.cancel"/></button>
    </form:form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
