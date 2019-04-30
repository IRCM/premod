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

<stripes:layout-render name="/WEB-INF/pages/layout/layout.jsp">
    <stripes:layout-component name="pageTitle">
        Search Predicted Modules
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
        <c:url var="ascImage" value="/img/sort/up.gif"/>
        <c:url var="descImage" value="/img/sort/down.gif"/>
        <style>
            div.layout_content div {margin:1em 0;}
            h1 {margin:2em 0 1em 0;}
            h2 {margin:1.5em 0 0.5em 0;}
            ul.messages {margin:1em 0;}
            table {margin:0.5em 0;}
            ul.summary {margin:0 0 1em 3em; padding:0;}
            div.image {margin:0.5em 0;}
            ul.navigation {margin:0; padding:0; list-style-type:none;}
            ul.navigation li {display:inline; margin:0 0.1em;}
            ul.navigation li.first {margin:0 0.5em 0 0;}
            ul.navigation li.last {margin:0 0 0 0.5em;}
        </style>
    </stripes:layout-component>
    <stripes:layout-component name="navbar">search</stripes:layout-component>
    <stripes:layout-component name="contents">
        <h1>
            Result of module search
        </h1>
        
        <stripes:errors/>
        <stripes:messages/>
        
        <h2>
            Request summary
        </h2>
        <ul class="summary">
            <li>
                Organism: <premod:organism organism="${actionBean.organism}"/>
            </li>
            <li>
                Version: <premod:version version="${actionBean.version}"/>
            </li>
            <c:if test="${actionBean.searchType eq 'ALL'}">
                <li>
                    All modules
                </li>
            </c:if>
            <c:if test="${actionBean.searchType eq 'MODULE' && !empty actionBean.moduleNames}">
                <li>
                    Module name(s):
                    <c:forEach var="name" items="${actionBean.moduleNames}" varStatus="iter">
                        <c:out value="${name}"/><c:if test="${!iter.last}">,</c:if>
                    </c:forEach>
                </li>
            </c:if>
            <c:if test="${actionBean.searchType eq 'UNIT' && !empty actionBean.unitIds}">
                <li>
                    Module matrix(ces):
                    <c:forEach var="id" items="${actionBean.unitIds}" varStatus="iter">
                        <c:out value="${id}"/><c:if test="${!iter.last}">,</c:if>
                    </c:forEach>
                    <br>
                    Matrices operator: <premod:conjunction conjunction="${actionBean.unitConjunction}"/>
                    <br>
                    Matrices type:
                    <c:if test="${empty actionBean.unitTag}">
                        All
                    </c:if>
                    <c:if test="${!empty actionBean.unitTag}">
                        <unit:tagType tagType="${actionBean.unitTag}"/>
                    </c:if>
                </li>
            </c:if>
            <c:if test="${actionBean.searchType eq 'GENE_NAME' && !empty actionBean.geneNames}">
                <li>
                    Gene name(s):
                    <c:forEach var="name" items="${actionBean.geneNames}" varStatus="iter">
                        <c:out value="${name}"/><c:if test="${!iter.last}">,</c:if>
                    </c:forEach>
                </li>
            </c:if>
            <c:if test="${actionBean.searchType eq 'GENE_ID' && !empty actionBean.geneIds}">
                <li>
                    Entrez Gene id(s):
                    <c:forEach var="id" items="${actionBean.geneIds}" varStatus="iter">
                        <c:out value="${id}"/><c:if test="${!iter.last}">,</c:if>
                    </c:forEach>
                </li>
            </c:if>
            <c:if test="${!empty actionBean.chromosomes}">
                <li>
                    Chromosome(s):
                    <c:forEach var="chromosome" items="${actionBean.chromosomes}" varStatus="iter">
                        <c:out value="${chromosome}"/><c:if test="${!iter.last}">,</c:if>
                    </c:forEach>
                </li>
            </c:if>
            <c:if test="${!empty actionBean.score}">
                <li>
                    Module score &gt;= <c:out value="${actionBean.score}"/>
                </li>
            </c:if>
            <c:if test="${!empty actionBean.geneUp}">
                <li>
                    <c:out value="${actionBean.geneUp}"/> Kb upstream of gene
                </li>
            </c:if>
            <c:if test="${!empty actionBean.geneDown}">
                <li>
                    <c:out value="${actionBean.geneDown}"/> Kb downstream of gene
                </li>
            </c:if>
            <c:if test="${!empty actionBean.overlapping}">
                <li>
                    <c:if test="${actionBean.overlapping eq 'OVERLAP'}">
                        Modules must overlap CpG island
                    </c:if>
                    <c:if test="${actionBean.overlapping eq 'NO_OVERLAP'}">
                        Modules must not overlap CpG island
                    </c:if>
                </li>
            </c:if>
        </ul>
        
        <c:if test="${actionBean.searchType eq 'GENE_NAME' && !empty actionBean.geneNames
            || actionBean.searchType eq 'GENE_ID' && !empty actionBean.geneIds}">
            <h2>
                Gene contexts
            </h2>
            <c:if test="${actionBean.searchType eq 'GENE_NAME'}">
                <c:forEach var="geneName" items="${actionBean.geneNames}">
                    <c:set var="image" value="${actionBean.geneContextImages[geneName]}"/>
                    <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.module.SearchModuleActionBean" event="geneNameImage">
                        <stripes:param name="geneName" value="${geneName}"/>
                        <stripes:param name="version" value="${actionBean.version}"/>
                    </stripes:url>
                    <div class="image ${geneName}">
                        <img src="${url}" width="${image.width}" height="${image.height}" alt="Gene ${geneName} context" usemap="#context_${geneName}"/>
                        <map name="context_${geneName}">
                            <c:forEach var="urlLocation" items="${image.urlLocations}" varStatus="urlIter">
                                <c:set var="location" value="${urlLocation.location}"/>
                                <area shape="rect" href="${urlLocation.url}" coords="${location.x},${location.y},${location.x+location.width},${location.y+location.height}" alt="${urlLocation.alt}">
                            </c:forEach>
                        </map>
                    </div>
                </c:forEach>
            </c:if>
            <c:if test="${actionBean.searchType eq 'GENE_ID'}">
                <c:forEach var="geneId" items="${actionBean.geneIds}">
                    <c:set var="image" value="${actionBean.geneContextImages[geneId]}"/>
                    <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.module.SearchModuleActionBean" event="geneIdImage">
                        <stripes:param name="geneId" value="${geneId}"/>
                        <stripes:param name="version" value="${actionBean.version}"/>
                    </stripes:url>
                    <div class="image ${geneId}">
                        <img src="${url}" width="${image.width}" height="${image.height}" alt="Gene ${geneId} context" usemap="#context_${geneId}"/>
                        <map name="context_${geneId}">
                            <c:forEach var="urlLocation" items="${image.urlLocations}" varStatus="urlIter">
                                <c:set var="location" value="${urlLocation.location}"/>
                                <area shape="rect" href="${urlLocation.url}" coords="${location.x},${location.y},${location.x+location.width},${location.y+location.height}" alt="${urlLocation.alt}">
                            </c:forEach>
                        </map>
                    </div>
                </c:forEach>
            </c:if>
        </c:if>
        
        <c:set var="moduleCount" value="${actionBean.modulesCount}"/>
        <c:if test="${moduleCount == 0}">
            <h2>
                No modules match search
            </h2>
        </c:if>
        <c:if test="${moduleCount > 0}">
            <h2>
                <c:choose>
                    <c:when test="${moduleCount le 1}">
                        <c:out value="${moduleCount}"/> module
                    </c:when>
                    <c:when test="${moduleCount le actionBean.modulesPerPage}">
                        <c:out value="${moduleCount}"/> modules
                    </c:when>
                    <c:otherwise>
                        Modules <fmt:formatNumber value="${(actionBean.page-1) * actionBean.modulesPerPage + 1}" pattern="#"/>
                        to <fmt:formatNumber value="${ircm:min(actionBean.page * actionBean.modulesPerPage, moduleCount)}" pattern="#"/>
                        out of <fmt:formatNumber value="${moduleCount}" pattern="#"/>
                    </c:otherwise>
                </c:choose>
            </h2>
            <table class="result">
                <thead>
                    <tr>
                        <th>
                            <c:set var="columnName" value="Name"/>
                            <c:set var="expectedSort" value="NAME"/>
                            <%@ include file="SearchModuleActionBean_Sort.jsp" %>
                        </th>
                        <th>
                            <c:set var="columnName" value="Chromosome"/>
                            <c:set var="expectedSort" value="POSITION"/>
                            <%@ include file="SearchModuleActionBean_Sort.jsp" %>
                        </th>
                        <th>
                            Length
                        </th>
                        <th>
                            <c:set var="columnName" value="Score"/>
                            <c:set var="expectedSort" value="SCORE"/>
                            <%@ include file="SearchModuleActionBean_Sort.jsp" %>
                        </th>
                        <th>
                            Upstream Entrez Gene id
                        </th>
                        <th>
                            Upstream gene name
                        </th>
                        <th>
                            Upstream gene Position
                        </th>
                        <th>
                            Downstream Entrez Gene id
                        </th>
                        <th>
                            Downstream gene name
                        </th>
                        <th>
                            Downstream gene position
                        </th>
                        <th colspan="5">
                            Tag matrices
                        </th>
                    </tr>
                </thead>
                <tbody>
                	<premod:forQueue var="moduleWithBestUnits" items="${actionBean.modules}">
                        <c:set var="module" value="${moduleWithBestUnits.module}"/>
                        <c:set var="bestUnits" value="${moduleWithBestUnits.bestUnits}"/>
                        <tr>
                            <td>
                                <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.module.ReadModuleActionBean">
                                    <stripes:param name="module" value="${module}"/>
                                </stripes:url>
                                <a href="${url}">
                                    <c:out value="${module.name}"/>
                                </a>
                            </td>
                            <td>
                                chr<c:out value="${module.chromosome}"/>:<c:out value="${module.start}"/>-<c:out value="${module.end}"/>
                            </td>
                            <td>
                                <c:out value="${module.length}"/>
                            </td>
                            <td>
                                <fmt:formatNumber value="${module.score}" pattern="#"/>
                            </td>
                            <td>
                                <premod:geneUrl var="url" geneId="${module.upstreamGeneLocus}"/>
                                <a href="${url}">
                                    <c:out value="${module.upstreamGeneLocus}"/>
                                </a>
                            </td>
                            <td>
                                <premod:geneUrl var="url" geneId="${module.upstreamGeneLocus}"/>
                                <a href="${url}">
                                    <c:out value="${module.upstreamGeneName}"/>
                                </a>
                            </td>
                            <td>
                                <c:out value="${module.upstreamGenePosition}"/>
                            </td>
                            <td>
                                <premod:geneUrl var="url" geneId="${module.downstreamGeneLocus}"/>
                                <a href="${url}">
                                    <c:out value="${module.downstreamGeneLocus}"/>
                                </a>
                            </td>
                            <td>
                                <premod:geneUrl var="url" geneId="${module.downstreamGeneLocus}"/>
                                <a href="${url}">
                                    <c:out value="${module.downstreamGeneName}"/>
                                </a>
                            </td>
                            <td>
                                <c:out value="${module.downstreamGenePosition}"/>
                            </td>
                            <c:forEach begin="0" end="4" varStatus="iter">
                                <c:set var="unit" value="${bestUnits[iter.index]}"/>
                                <c:if test="${!empty unit}">
                                    <td>
                                        <premod:transfacUrl var="url" unit="${unit}"/>
                                        <a href="${url}">
                                            <c:out value="${unit.id}"/>
                                        </a>
                                        (<c:out value="${unit.name}"/>)
                                    </td>
                                </c:if>
                                <c:if test="${empty unit}">
                                    <td>&nbsp;</td>
                                </c:if>
                            </c:forEach>
                        </tr>
                	</premod:forQueue>
                </tbody>
            </table>
            
            <div>
                Go to page: <%@ include file="SearchModuleActionBean_Navigation.jsp" %>
            </div>
        </c:if>
    </stripes:layout-component>
</stripes:layout-render>
