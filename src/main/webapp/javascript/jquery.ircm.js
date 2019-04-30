/*
 * Copyright (c) 2013 Institut de recherches cliniques de Montreal (IRCM)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * Returns checked attribute.
 */
jQuery.fn.checked = function() {
    return this.attr("checked");
};

/**
 * Check checkbox or radio.
 */
jQuery.fn.check = function() {
    return this.attr("checked", "checked");
};

/**
 * Uncheck checkbox or radio.
 */
jQuery.fn.uncheck = function() {
    return this.attr("checked", "");
};

/**
 * Add alt class to every odd elements.
 * alt class should change color of current element.
 */
jQuery.fn.zebra = function() {
    if($(this).is("table")) {
        if ($(this).find("li").length > 0) {
            var trs = $(this).find("tbody tr");
            var alt = false;
            trs.each(function(){
                if ($(this).find("li").length > 0) {
                    var lis = $(this).find("li");
                    lis.each(function(){
                        if (alt) {
                            $(this).addClass("alt");
                            alt = false;
                        }
                        else {
                            alt = true;
                        }
                    });
                }
                else {
                    if (alt) {
                        $(this).addClass("alt");
                        alt = false;
                    }
                    else {
                        alt = true;
                    }
                }
            });
        }
        else {
            $(this).find("tbody tr:odd").addClass("alt");
        }
    }
    if ($(this).is("tbody")) {
        $(this).find("tr:odd").addClass("alt");
    }
    if ($(this).is("tr")) {
        $(this).filter(":odd").addClass("alt");
    }
};
