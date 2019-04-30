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

<stripes:layout-definition>
    <html lang="en">
        <head>
            <title>${pageTitle}</title>
            <%@ include file="head.jsp" %>
            <stripes:layout-component name="head"/>
        </head>
        <body>
            <stripes:layout-component name="header">
                <%@ include file="header.jsp" %>
            </stripes:layout-component>
            
            <stripes:layout-component name="menu">
                <%@ include file="menu.jsp" %>
            </stripes:layout-component>
            
            <div class="layout_content">
                <stripes:layout-component name="contents"/>
            </div>
            
            <stripes:layout-component name="footer">
                <%@ include file="footer.jsp" %>
            </stripes:layout-component>
        </body>
    </html>
</stripes:layout-definition>
