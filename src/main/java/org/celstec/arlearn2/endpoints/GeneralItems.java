package org.celstec.arlearn2.endpoints;


import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.DependencyWrapper;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.generalItem.*;
import org.celstec.arlearn2.beans.run.GeneralItemVisibilityList;
import org.celstec.arlearn2.delegators.GeneralItemDelegator;
import org.celstec.arlearn2.delegators.GeneralItemVisibilityDelegator;
import org.celstec.arlearn2.endpoints.util.EnhancedUser;
import org.codehaus.jettison.json.JSONException;

/**
 * ****************************************************************************
 * Copyright (C) 2019 Open Universiteit Nederland
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

@Api(name = "generalItems")
public class GeneralItems extends GenericApi {

    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.GET,
            name = "gameGeneralItems",
            path = "/generalItems/gameId/{gameId}"
    )
    public GeneralItemList getItems(@Named("gameId") Long gameId) {
        //TODO check if use has access to game
        GeneralItemDelegator gid = new GeneralItemDelegator();
        return gid.getGeneralItems(gameId);
    }


    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.GET,
            name = "gameGeneralItemsWithCursor",
            path = "/generalItems/gameId/{gameId}/cursor/{cursor}"
    )
    public GeneralItemList getItemsWithCursor(final EnhancedUser user,
                                              @Named("gameId") Long gameId,
                                              @Named("cursor") String cursor) {
        if (cursor == null || cursor.length() < 3) {
            cursor = null;
        }
        GeneralItemDelegator gid = new GeneralItemDelegator();
        return gid.getGeneralItems(gameId, cursor);
    }

    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.GET,
            name = "runVisibleItems",
            path = "/generalItems/visible/runId/{runId}"
    )
    public GeneralItemVisibilityList getVisibilityStatements(final EnhancedUser user, @Named("runId") Long runId) {
        GeneralItemVisibilityDelegator vidDel = new GeneralItemVisibilityDelegator();
        return vidDel.getVisibleItems(runId, user.createFullId(), null, null);
    }


    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.DELETE,
            name = "delete_item",
            path = "/generalItems/gameId/{gameId}/itemId/{itemId}"
    )
    public GeneralItem delete(final EnhancedUser user, @Named("gameId") Long gameId, @Named("itemId") Long itemId){
        return new GeneralItemDelegator().deleteGeneralItemNew(gameId, itemId, user.createFullId());
    }

    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.GET,
            name = "get_item",
            path = "/generalItems/itemId/{itemId}"
    )
    public GeneralItem getMessage(final EnhancedUser user, @Named("itemId") Long itemId){
        return new GeneralItemDelegator().getGeneralItem(itemId);
    }

    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.POST,
            name = "update_narator",
            path = "/generalItems/createNarrator"
    )
    public GeneralItem createNarrator(final User user, NarratorItem item){
 //todo check access rights
        return new GeneralItemDelegator().createGeneralItem(item);
    }

    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.POST,
            name = "update_single_choice",
            path = "/generalItems/createSingleChoice"
    )
    public GeneralItem createSingleChoice(final User user, SingleChoiceTest item){
        return new GeneralItemDelegator().createGeneralItem(item);
    }

    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.POST,
            name = "update_multiple_choice",
            path = "/generalItems/createMultipleChoice"
    )
    public GeneralItem createMultipleChoice(final User user, MultipleChoiceTest item){
        return new GeneralItemDelegator().createGeneralItem(item);
    }

    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.POST,
            name = "update_single_choice_image",
            path = "/generalItems/createSingleChoiceImage"
    )
    public GeneralItem createSingleChoiceImage(final User user, SingleChoiceImageTest item){
        return new GeneralItemDelegator().createGeneralItem(item);
    }

    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.POST,
            name = "update_multiple_choice_image",
            path = "/generalItems/createMultipleChoiceImage"
    )
    public GeneralItem createMultipleChoiceImage(final User user, MultipleChoiceImageTest item){
        return new GeneralItemDelegator().createGeneralItem(item);
    }


    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.POST,
            name = "update_video_object",
            path = "/generalItems/createVideoObject"
    )
    public GeneralItem createVideoObject(final User user, VideoObject item){
        return new GeneralItemDelegator().createGeneralItem(item);
    }

    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.POST,
            name = "update_audio_object",
            path = "/generalItems/createAudioObject"
    )
    public GeneralItem createAudioObject(final User user, AudioObject item){
        return new GeneralItemDelegator().createGeneralItem(item);
    }


    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.POST,
            name = "update_scan_tage",
            path = "/generalItems/createScanTag"
    )
    public GeneralItem createScanTag(final User user, ScanTag item){
        return new GeneralItemDelegator().createGeneralItem(item);
    }


    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.POST,
            name = "dependsOn",
            path = "/generalItems/wrapperpost"
    )
    public GeneralItem dependsOn(final User user, DependencyWrapper item){

        System.out.println(item.getDependencyAsString());
        JsonBeanDeserializer jbd;
        try {
            jbd = new JsonBeanDeserializer(item.getDependencyAsString());
            GeneralItem generalItem = (GeneralItem) jbd.deserialize(GeneralItem.class);
            System.out.println(generalItem.toString());

            return new GeneralItemDelegator().createGeneralItem(generalItem);
        } catch (JSONException e) {
            System.out.println("json exception" + e);
            System.out.println(e);
            e.printStackTrace();

            System.out.println(e.getLocalizedMessage());
        } catch (Exception e) {
            System.out.println(" exception" + e);
            System.out.println(e);
            e.printStackTrace();

            System.out.println(e.getLocalizedMessage());
        }

        return null;
    }

//    public static void main(String[] args) {
//        String json = "{\"type\":\"org.celstec.arlearn2.beans.generalItem.PictureQuestion\",\"gameId\":5634472569470976,\"deleted\":false,\"lastModificationDate\":1589195104968,\"id\":5661854663704576,\"scope\":\"user\",\"name\":\"reply with picture test\",\"description\":\"next\",\"label\":\"\",\"autoLaunch\":false,\"fileReferences\":[{\"key\":\"background\",\"type\":\"org.celstec.arlearn2.beans.generalItem.FileReference\",\"fileReference\":\"/game/5634472569470976/Resultaat A-Book fullscreen.png\"}],\"message\":true,\"showFeedback\":false,\"richText\":\"testx\"}\n";
//        System.out.println("tt " + json);
//
//        JsonBeanDeserializer jbd;
//        try {
//            jbd = new JsonBeanDeserializer(json);
//            GeneralItem generalItem = (GeneralItem) jbd.deserialize(GeneralItem.class);
//            System.out.println(generalItem.toString());
//
//
//        } catch (JSONException e) {
//            System.out.println("json exception" + e);
//            System.out.println(e);
//            e.printStackTrace();
//
//            System.out.println(e.getLocalizedMessage());
//        } catch (Exception e) {
//            System.out.println(" exception" + e);
//            System.out.println(e);
//            e.printStackTrace();
//
//            System.out.println(e.getLocalizedMessage());
//        }
//    }

//    dependsOn: {type: "org.celstec.arlearn2.beans.dependencies.ActionDependency", action: "read",…}
//    action: "read"
//    generalItemId: "5715999101812736"
//    type: "org.celstec.arlearn2.beans.dependencies.ActionDependency"


}


