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

<c:set var="module" value="${actionBean.module}"/>
<stripes:layout-render name="/WEB-INF/pages/layout/layout.jsp">
    <stripes:layout-component name="pageTitle">
        Module's Units
    </stripes:layout-component>
    <stripes:layout-component name="head">
        <meta name="robots" content="NONE"/>
        <script type="text/javascript">
            <!--
                $(function(){
                    zebra();
                });
            //-->
        </script>
        <style>
            div.layout_content div {margin:1em 0;}
            h1 {margin:2em 0 1em 0;}
            h2 {margin:1.5em 0 0.5em 0;}
            h3 {margin:1em 0 0 0;}
            ul {margin:1em 0;}
            table {margin:0.5em 0;}
        </style>
    </stripes:layout-component>
    <stripes:layout-component name="navbar">search</stripes:layout-component>
    <stripes:layout-component name="contents">
        <h1>
            Unit Occurrences for Module <c:out value="${module.name}"/>
        </h1>
        
        <ul class="other_views">
            <module:ucscUrl var="url" module="${module}"/>
            <li>
                <a href="${url}">
                    Link to UCSC genome Browser
                </a>
            </li>
            <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.module.ReadModuleActionBean">
                <stripes:param name="module" value="${module}"/>
            </stripes:url>
            <li>
                <a href="${url}">
                    Link to Module
                </a>
            </li>
        </ul>
        
        <h2>
            Module information
        </h2>
        <table class="result">
            <tbody>
                <tr>
                    <th>Organism</th>
                    <td>
                        <premod:organism organism="${module.organism}"/>
                    </td>
                </tr>
                <tr>
                    <th>Name</th>
                    <td>
                        <c:out value="${module.name}"/>
                    </td>
                </tr>
                <tr>
                    <th>Type</th>
                    <td>
                        <module:type type="${module.type}"/>
                    </td>
                </tr>
                <tr>
                    <th>Chromosome</th>
                    <td>
                        chr<c:out value="${module.chromosome}"/>:<c:out value="${module.start}"/>-<c:out value="${module.end}"/>
                    </td>
                </tr>
                <tr>
                    <th>Genome Assembly</th>
                    <td>
                        <c:out value="${module.assembly}"/>
                    </td>
                </tr>
                <tr>
                    <th>Score</th>
                    <td>
                        <fmt:formatNumber value="${module.score}" pattern="#"/>
                    </td>
                </tr>
                <tr>
                    <th>Length</th>
                    <td>
                        <c:out value="${module.length}"/>
                    </td>
                </tr>
                <tr>
                    <th>CpG Island</th>
                    <td>
                        <module:cpgIsland cpgIsland="${module.cpgIsland}"/>
                    </td>
                </tr>
            </tbody>
        </table>
        
        <h2>
            Tag Matrix Occurrences
        </h2>
        
        <c:forEach var="unit" items="${actionBean.bestUnits}">
            <a name="A_${unit.id}"></a>
            <h3>
                <premod:transfacUrl var="url" unit="${unit}"/>
                <a href="${url}">
                    Matrix <c:out value="${unit.id}"/>
                </a>
            </h3>
            <table class="result">
                <thead>
                    <tr>
                        <th>Start</th>
                        <th>End</th>
                        <th>Strand</th>
                        <th>Occurrence score</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="occurrence" items="${actionBean.unitOccurrences[unit]}">
                        <tr>
                            <td><c:out value="${occurrence.start}"/></td>
                            <td><c:out value="${occurrence.end}"/></td>
                            <td><c:out value="${occurrence.strand}"/></td>
                            <td><fmt:formatNumber value="${occurrence.occScore}" pattern="#"/></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:forEach>
        
        <h2>
            Other Matrix Occurrences
        </h2>
        
        <c:forEach var="unit" items="${actionBean.nonBestUnits}">
            <a name="A_${unit.id}"></a>
            <h3>
                <premod:transfacUrl var="url" unit="${unit}"/>
                <a href="${url}">
                    Matrix <c:out value="${unit.id}"/>
                </a>
            </h3>
            <table class="result">
                <thead>
                    <tr>
                        <th>Start</th>
                        <th>End</th>
                        <th>Strand</th>
                        <th>Occurrence score</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="occurrence" items="${actionBean.unitOccurrences[unit]}">
                        <tr>
                            <td><c:out value="${occurrence.start}"/></td>
                            <td><c:out value="${occurrence.end}"/></td>
                            <td><c:out value="${occurrence.strand}"/></td>
                            <td><fmt:formatNumber value="${occurrence.occScore}" pattern="#"/></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:forEach>
        
    </stripes:layout-component>
</stripes:layout-render>
