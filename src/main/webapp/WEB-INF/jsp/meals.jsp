<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<script type="text/javascript" src="resources/js/datatablesUtil.js" defer></script>
<script type="text/javascript" src="resources/js/mealDatatables.js" defer></script>

<jsp:include page="fragments/bodyHeader.jsp"/>

    <div class="jumbotron">
        <div class="container">

            <h3 class="page-header"><spring:message code="meal.title"/></h3>

            <form id="filter-form">
                <div class="form-group left">
                    <label for="startDate" class="control-label col-lg-3 col-md-3 col-sm-3 col-xs-3"><spring:message
                            code="meal.startDate"/>:</label>
                    <div class="col-lg-3 col-md-3 col-sm-3 col-xs-3">
                        <input type="date" class="form-control" name="startDate" value="${param.startDate}"
                               id="startDate">
                    </div>
                </div>

                <div class="form-group right">
                    <label for="endDate" class="control-label col-lg-3 col-md-3 col-sm-3 col-xs-3"><spring:message
                            code="meal.endDate"/>:</label>
                    <div class="col-lg-3 col-md-3 col-sm-3 col-xs-3">
                        <input type="date" class="form-control" name="endDate" value="${param.endDate}" id="endDate">
                    </div>
                </div>

                <div class="form-group left">
                    <label for="startTime" class="control-label col-lg-3 col-md-3 col-sm-3 col-xs-3"><spring:message
                            code="meal.startTime"/>:</label>
                    <div class="col-lg-3 col-md-3 col-sm-3 col-xs-3">
                        <input type="time" class="form-control" name="startTime" value="${param.startDate}"
                               id="startTime">
                    </div>
                </div>

                <div class="form-group right">
                    <label for="endTime" class="control-label col-lg-3 col-md-3 col-sm-3 col-xs-3"><spring:message
                            code="meal.endTime"/>:</label>
                    <div class="col-lg-3 col-md-3 col-sm-3 col-xs-3">
                        <input type="time" class="form-control" name="endTime" value="${param.startDate}" id="endTime">
                    </div>
                </div>

                <div class="form-group">
                    <div class="btn-group">
                        <button type="submit" class="btn btn-success">
                            <span class="glyphicon glyphicon-filter" aria-hidden="true"></span>
                            <spring:message code="meal.filter"/>
                        </button>
                        <button type="button" class="btn btn-primary" onclick="updateTable()">
                            <span class="glyphicon glyphicon-refresh" aria-hidden="true"></span>
                            <spring:message code="common.refresh"/>
                        </button>
                    </div>
                </div>
            </form>

            <a class="btn btn-success" onclick="add()">
                <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                <spring:message code="meal.add"/>
            </a>

            <hr>
            <table class="table table-striped display" id="meals-datatable">
                <thead>
                <tr>
                    <th><spring:message code="meal.dateTime"/></th>
                    <th><spring:message code="meal.description"/></th>
                    <th><spring:message code="meal.calories"/></th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <c:forEach items="${meals}" var="meal">
                    <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealWithExceed"/>
                    <tr class="${meal.exceed ? 'exceeded' : 'normal'}" id="${meal.id}">
                        <td>
                                <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                                <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                                <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                                ${fn:formatDateTime(meal.dateTime)}
                        </td>
                        <td>${meal.description}</td>
                        <td>${meal.calories}</td>
                        <td><a><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a></td>
                        <td><a class="delete"><span class="glyphicon glyphicon-remove"
                                                    aria-hidden="true"></span></a></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
<jsp:include page="mealForm.jsp"/>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>