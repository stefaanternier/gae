package org.celstec.arlearn2.jdo.manager;

import com.google.appengine.api.datastore.*;
//import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.store.Category;
import org.celstec.arlearn2.beans.store.CategoryList;
import org.celstec.arlearn2.jdo.PMF;

import org.celstec.arlearn2.jdo.classes.CategoryEntity;

//import javax.jdo.PersistenceManager;
import java.util.ArrayList;
import java.util.List;

/**
 * ****************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * <p/>
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Contributors: Stefaan Ternier
 * ****************************************************************************
 */
public class CategoryManager {
    private static DatastoreService datastore;
    static {
        datastore = DatastoreServiceFactory.getDatastoreService();
    }

    public static Category addCategory(Long categoryId, String title, String lang) {
        CategoryEntity categoryJDO = new CategoryEntity();
        categoryJDO.setCategoryId(categoryId);
        categoryJDO.setTitle(title);
        categoryJDO.setLang(lang);
        categoryJDO.setDeleted(false);
        Entity entity = categoryJDO.toEntity();
        Key key =datastore.put(entity);
        categoryJDO.setId(key);
        return categoryJDO.toCategory();
    }


    public static List<CategoryEntity> getCategoryList(String lang) {
        return null;
    }

    public static Category getCategory(long id) {
        Key key = KeyFactory.createKey(CategoryEntity.KIND,id);

        Entity result = null;
        try {
            result = datastore.get(key);
        } catch (EntityNotFoundException e) {

            Category categoryError = new Category();
            categoryError.setError("Category does not exist");
            return categoryError;
        }
        return new CategoryEntity(result).toCategory();
    }

    public static CategoryList getCategories(String lang) {

        CategoryList resultList = new CategoryList();
        Query q = new Query(CategoryEntity.KIND)
                .setFilter(new Query.FilterPredicate(CategoryEntity.COL_LANG, Query.FilterOperator.EQUAL, lang));
        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            resultList.addCategory(new CategoryEntity(result).toCategory());
        }
        return resultList;
    }

    public static CategoryList getCategories(String lang, Long categoryId) {

        CategoryList resultList = new CategoryList();
        Query.CompositeFilter filter;

        filter = Query.CompositeFilterOperator.and(
                new Query.FilterPredicate(CategoryEntity.COL_LANG, Query.FilterOperator.EQUAL, lang),
                new Query.FilterPredicate(CategoryEntity.COL_CATEGORYID, Query.FilterOperator.EQUAL, categoryId)
        );

        Query q = new Query(CategoryEntity.KIND)
                .setFilter(filter);
        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            resultList.addCategory(new CategoryEntity(result).toCategory());
        }
        return resultList;
    }

    public static CategoryList getCategories(Long id) {
        CategoryList resultList = new CategoryList();
        Query q = new Query(CategoryEntity.KIND)
                .setFilter(new Query.FilterPredicate(CategoryEntity.COL_CATEGORYID, Query.FilterOperator.EQUAL, id));
        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            resultList.addCategory(new CategoryEntity(result).toCategory());
        }
        return resultList;
    }
}
