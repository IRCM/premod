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
        Contact information
    </stripes:layout-component>
    <stripes:layout-component name="head">
        <meta name="robots" content="ALL"/>
        <style>
            h1 {margin:1.5em 0;}
            h2 {margin:1em 0 0.3em 0;}
            p.contact {margin:0.3em 0 1em 0;}
        </style>
    </stripes:layout-component>
    <stripes:layout-component name="navbar">contact</stripes:layout-component>
    <stripes:layout-component name="contents">
        <h1>
            Contact information
        </h1>
        
        <h2>
            For any questions, comments or suggestion, please contact
        </h2>
        <p class="contact">
            Mathieu Blanchette
            <br>
            McGill
            <br>
            <a href="mailto:blanchem@mcb.mcgill.ca" class="email">
                email: blanchem@mcb.mcgill.ca
            </a>
        </p>
        
        <h2>
            For reporting bug on the web site, contact
        </h2>
        <p class="contact">
            Christian Poitras
            <br>
            IRCM
            <br>
            <a href="mailto:christian.poitras@ircm.qc.ca" class="email">
                email: christian.poitras@ircm.qc.ca
            </a>
        </p>
    </stripes:layout-component>
</stripes:layout-render>
