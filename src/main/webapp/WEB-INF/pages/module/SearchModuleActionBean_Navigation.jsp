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

<ul class="navigation">
    <c:if test="${actionBean.page gt 6}">
        <li class="first">
            <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.module.SearchModuleActionBean" event="search">
                <stripes:param name="organism" value="${actionBean.organism}"/>
                <stripes:param name="version" value="${actionBean.version}"/>
                <stripes:param name="searchType" value="${actionBean.searchType}"/>
                <stripes:param name="moduleNames" value="${actionBean.moduleNames}"/>
                <stripes:param name="unitIds" value="${actionBean.unitIds}"/>
                <stripes:param name="unitConjunction" value="${actionBean.unitConjunction}"/>
                <stripes:param name="unitTag" value="${actionBean.unitTag}"/>
                <stripes:param name="geneNames" value="${actionBean.geneNames}"/>
                <stripes:param name="geneIds" value="${actionBean.geneIds}"/>
                <stripes:param name="chromosomes" value="${actionBean.chromosomes}"/>
                <stripes:param name="score" value="${actionBean.score}"/>
                <stripes:param name="geneUp" value="${actionBean.geneUp}"/>
                <stripes:param name="geneDown" value="${actionBean.geneDown}"/>
                <stripes:param name="overlapping" value="${actionBean.overlapping}"/>
                <stripes:param name="format" value="${actionBean.format}"/>
                <stripes:param name="sort" value="${actionBean.sort}"/>
                <stripes:param name="descending" value="${actionBean.descending}"/>
                <stripes:param name="page" value="1"/>
            </stripes:url>
            <a href="${url}">
                first
            </a>
        <li>
    </c:if>
    <c:set var="pageMax" value="${ircm:ceil(actionBean.modulesCount / actionBean.modulesPerPage)}"/>
    <c:set var="begin" value="${ircm:max(actionBean.page - 10, 1)}"/>
    <c:set var="end" value="${ircm:min(actionBean.page + 10, pageMax)}"/>
    <c:forEach var="i" begin="${begin}" end="${end}">
        <c:if test="${i eq actionBean.page}">
            <li>
                ${i}
            </li>
        </c:if>
        <c:if test="${i ne actionBean.page}">
            <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.module.SearchModuleActionBean" event="search">
                <stripes:param name="organism" value="${actionBean.organism}"/>
                <stripes:param name="version" value="${actionBean.version}"/>
                <stripes:param name="searchType" value="${actionBean.searchType}"/>
                <stripes:param name="moduleNames" value="${actionBean.moduleNames}"/>
                <stripes:param name="unitIds" value="${actionBean.unitIds}"/>
                <stripes:param name="unitConjunction" value="${actionBean.unitConjunction}"/>
                <stripes:param name="unitTag" value="${actionBean.unitTag}"/>
                <stripes:param name="geneNames" value="${actionBean.geneNames}"/>
                <stripes:param name="geneIds" value="${actionBean.geneIds}"/>
                <stripes:param name="chromosomes" value="${actionBean.chromosomes}"/>
                <stripes:param name="score" value="${actionBean.score}"/>
                <stripes:param name="geneUp" value="${actionBean.geneUp}"/>
                <stripes:param name="geneDown" value="${actionBean.geneDown}"/>
                <stripes:param name="overlapping" value="${actionBean.overlapping}"/>
                <stripes:param name="format" value="${actionBean.format}"/>
                <stripes:param name="sort" value="${actionBean.sort}"/>
                <stripes:param name="descending" value="${actionBean.descending}"/>
                <stripes:param name="page" value="${i}"/>
            </stripes:url>
            <li>
                <a href="${url}">
                    ${i}
                </a>
            </li>
        </c:if>
    </c:forEach>
    <c:if test="${actionBean.page lt pageMax - 5}">
        <li class="last">
            <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.module.SearchModuleActionBean" event="search">
                <stripes:param name="organism" value="${actionBean.organism}"/>
                <stripes:param name="version" value="${actionBean.version}"/>
                <stripes:param name="searchType" value="${actionBean.searchType}"/>
                <stripes:param name="moduleNames" value="${actionBean.moduleNames}"/>
                <stripes:param name="unitIds" value="${actionBean.unitIds}"/>
                <stripes:param name="unitConjunction" value="${actionBean.unitConjunction}"/>
                <stripes:param name="unitTag" value="${actionBean.unitTag}"/>
                <stripes:param name="geneNames" value="${actionBean.geneNames}"/>
                <stripes:param name="geneIds" value="${actionBean.geneIds}"/>
                <stripes:param name="chromosomes" value="${actionBean.chromosomes}"/>
                <stripes:param name="score" value="${actionBean.score}"/>
                <stripes:param name="geneUp" value="${actionBean.geneUp}"/>
                <stripes:param name="geneDown" value="${actionBean.geneDown}"/>
                <stripes:param name="overlapping" value="${actionBean.overlapping}"/>
                <stripes:param name="format" value="${actionBean.format}"/>
                <stripes:param name="sort" value="${actionBean.sort}"/>
                <stripes:param name="descending" value="${actionBean.descending}"/>
                <stripes:param name="page" value="${pageMax}"/>
            </stripes:url>
            <a href="${url}">
                last
            </a>
        <li>
    </c:if>
</ul>
