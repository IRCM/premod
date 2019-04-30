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

<ul class="sf-menu">
    <li class="${navbar eq 'home' ? 'current' : ''}">
        <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.RootActionBean"/>
        <a href="${url}">
            Home
        </a>
    </li>
    <li class="${navbar eq 'search' ? 'current' : ''}">
        <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.module.SearchModuleActionBean"/>
        <a href="${url}">
            Search Predicted Modules
        </a>
    </li>
    <li class="${navbar eq 'statistic' ? 'current' : ''}">
        <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.unit.UnitStatisticActionBean"/>
        <a href="${url}">
            Statistics
        </a>
    </li>
    <li class="${navbar eq 'contact' ? 'current' : ''}">
        <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.ContactActionBean"/>
        <a href="${url}">
            Contact Us
        </a>
    </li>
    <li class="${navbar eq 'download' ? 'current' : ''}">
        <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.DownloadActionBean"/>
        <a href="${url}">
            Download
        </a>
    </li>
</ul>
