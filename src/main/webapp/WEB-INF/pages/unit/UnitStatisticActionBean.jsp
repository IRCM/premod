<%--

    Copyright (c) 2013 Institut de recherches cliniques de Montreal (IRCM)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ include file="/WEB-INF/pages/common/TagLibs.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:set var="organism" value="${actionBean.organism}"/>
<c:set var="version" value="${actionBean.version}"/>
<stripes:layout-render name="/WEB-INF/pages/layout/layout.jsp">
    <stripes:layout-component name="pageTitle">
        Statistics
    </stripes:layout-component>
    <stripes:layout-component name="head">
        <meta name="robots" content="INDEX,NOFOLLOW"/>
        <script type="text/javascript">
            <!--
                $(function(){
                    zebra();
                });
            -->
        </script>
        <style>
            h1 {margin:2em 0 1em 0; padding:0;}
            h1 a.excel {margin-left:5em; font-weight:normal;}
            h2 {margin:1.5em 0 0.5em 0; padding:0;}
            ul {margin:1em 0;}
            table {margin:0 0 1em 0;}
        </style>
    </stripes:layout-component>
    <stripes:layout-component name="navbar">statistic</stripes:layout-component>
    <stripes:layout-component name="contents">
        <h1>
            <c:if test="${organism == 'HUMAN' && version == 'LATEST'}">
                Human matrix frequencies Version 1.0 (latest)
            </c:if>
            <c:if test="${organism == 'HUMAN' && version == 'ARTICLE'}">
                Human matrix frequencies Version 0.0 (<a href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=pubmed&cmd=Retrieve&dopt=AbstractPlus&list_uids=16606704&query_hl=1&itool=pubmed_docsum">Blanchette et al.</a>)
            </c:if>
            <c:if test="${organism == 'MOUSE' && version == 'LATEST'}">
                Mouse matrix frequencies Version 1.0 (latest)
            </c:if>
            <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.unit.UnitStatisticActionBean" event="excel">
                <stripes:param name="organism" value="${organism}"/>
                <stripes:param name="version" value="${version}"/>
            </stripes:url>
            <a href="${url}" class="excel">
                Excel Format
            </a>
        </h1>
        
        <ul class="other_views">
            <c:if test="${organism != 'HUMAN' || version != 'LATEST'}">
                <li>
                    <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.unit.UnitStatisticActionBean">
                        <stripes:param name="organism" value="HUMAN"/>
                        <stripes:param name="version" value="LATEST"/>
                    </stripes:url>
                    <a href="${url}">
                        Link to human matrix frequencies Version 1.0 (latest)
                    </a>
                </li>
            </c:if>
            <c:if test="${organism != 'HUMAN' || version != 'ARTICLE'}">
                <li>
                    <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.unit.UnitStatisticActionBean">
                        <stripes:param name="organism" value="HUMAN"/>
                        <stripes:param name="version" value="ARTICLE"/>
                    </stripes:url>
                    <a href="${url}">
                        Link to human matrix frequencies Version 0.0
                    </a>
                    (<a href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=pubmed&cmd=Retrieve&dopt=AbstractPlus&list_uids=16606704&query_hl=1&itool=pubmed_docsum">Blanchette et al.</a>)
                </li>
            </c:if>
            <c:if test="${organism != 'MOUSE' || version != 'LATEST'}">
                <li>
                    <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.unit.UnitStatisticActionBean">
                        <stripes:param name="organism" value="MOUSE"/>
                        <stripes:param name="version" value="LATEST"/>
                    </stripes:url>
                    <a href="${url}">
                        Link to mouse matrix frequencies Version 1.0 (latest)
                    </a>
                </li>
            </c:if>
        </ul>
        
        <h2>
            Matrix Frequencies
        </h2>
        <table class="result">
            <thead>
                <tr>
                    <th>
                        <c:set var="columnName" value="Id"/>
                        <c:set var="expectedSort" value="ID"/>
                        <%@ include file="UnitStatisticActionBean_Sort.jsp" %>
                    </th>
                    <th>
                        <c:set var="columnName" value="Name"/>
                        <c:set var="expectedSort" value="NAME"/>
                        <%@ include file="UnitStatisticActionBean_Sort.jsp" %>
                    </th>
                    <th>
                        Description
                    </th>
                    <th>
                        <c:set var="columnName" value="Tag"/>
                        <c:set var="expectedSort" value="TAG"/>
                        <%@ include file="UnitStatisticActionBean_Sort.jsp" %>
                    </th>
                    <th>
                        <c:set var="columnName" value="Other"/>
                        <c:set var="expectedSort" value="OTHER"/>
                        <%@ include file="UnitStatisticActionBean_Sort.jsp" %>
                    </th>
                    <th>
                        <c:set var="columnName" value="Total"/>
                        <c:set var="expectedSort" value="TOTAL"/>
                        <%@ include file="UnitStatisticActionBean_Sort.jsp" %>
                    </th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="unitWithExtra" items="${actionBean.unitsWithExtra}">
                    <c:set var="unit" value="${unitWithExtra.unit}"/>
                    <c:set var="extra" value="${unitWithExtra.extra}"/>
                    <tr>
                        <td>
                            <premod:transfacUrl var="url" unit="${unit}"/>
                            <a href="${url}">
                                <c:out value="${unit.id}"/>
                            </a>
                        </td>
                        <td><c:out value="${unit.name}"/></td>
                        <td><c:out value="${unit.description}"/></td>
                        <td>
                            <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.unit.ModuleByUnitActionBean">
                                <stripes:param name="unit" value="${unit}"/>
                                <stripes:param name="organism" value="${organism}"/>
                                <stripes:param name="version" value="${version}"/>
                            </stripes:url>
                            <a href="${url}">
                                <c:out value="${extra.tag}"/>
                            </a>
                        </td>
                        <td><c:out value="${extra.noTag}"/></td>
                        <td><c:out value="${extra.any}"/></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </stripes:layout-component>
</stripes:layout-render>
