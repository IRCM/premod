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

<div class="layout_header">
    <map id="headerMap" name="headerMap">
        <c:url var="url" value="/"/>
        <area shape=rect coords="0,0,240,60" href="${url}" class="home" title="PReMod"/>
        <area shape=rect coords="315,0,465,60" href="http://www.ircm.qc.ca" alt="IRCM"/>
        <area shape=rect coords="475,0,630,60" href="http://www.mcgill.ca" alt="McGill"/>
        <area shape=rect coords="635,0,712,60" href="http://www.genomecanada.ca" alt="Genome Canada"/>
        <area shape=rect coords="713,0,785,60" href="http://www.genomequebec.com" alt="G�nome Qu�bec"/>
    </map>
    <c:url var="image" value="/img/header.png"/>
    <img src="${image}" alt="PReMod" usemap="#headerMap" width="785" height="60" usemap="#headerMap"/>
</div>
