package fr.joffreylagut.itemdisplayer.utilities;

import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import fr.joffreylagut.itemdisplayer.models.Photo;

import static junit.framework.Assert.assertEquals;

/**
 * FetchDataFromServerIntentServiceUnitTest.java
 * Purpose: Handle the unit tests of FetchDataFromServerIntentService.java
 *
 * @author Joffrey LAGUT
 * @version 1.0 2017-03-26
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class FetchDataFromServerIntentServiceUnitTest {

    @Test
    public void convertJSONResponseInPhoto() throws Exception {
        // Mock
        PowerMockito.mockStatic(Log.class);

        // Preparation
        int amountOfPhotoInJSON = 6;
        String jsonExample = "[{\"albumId\":1,\"id\":1,\"title\":\"accusamus beatae ad facilis cum similique qui sunt\",\"url\":\"http://placehold.it/600/92c952\",\"thumbnailUrl\":\"http://placehold.it/150/92c952\"},{\"albumId\":1,\"id\":2,\"title\":\"reprehenderit est deserunt velit ipsam\",\"url\":\"http://placehold.it/600/771796\",\"thumbnailUrl\":\"http://placehold.it/150/771796\"},{\"albumId\":1,\"id\":3,\"title\":\"officia porro iure quia iusto qui ipsa ut modi\",\"url\":\"http://placehold.it/600/24f355\",\"thumbnailUrl\":\"http://placehold.it/150/24f355\"},{\"albumId\":1,\"id\":4,\"title\":\"culpa odio esse rerum omnis laboriosam voluptate repudiandae\",\"url\":\"http://placehold.it/600/d32776\",\"thumbnailUrl\":\"http://placehold.it/150/d32776\"},{\"albumId\":1,\"id\":5,\"title\":\"natus nisi omnis corporis facere molestiae rerum in\",\"url\":\"http://placehold.it/600/f66b97\",\"thumbnailUrl\":\"http://placehold.it/150/f66b97\"},{\"albumId\":1,\"id\":6,\"title\":\"accusamus ea aliquid et amet sequi nemo\",\"url\":\"http://placehold.it/600/56a8c2\",\"thumbnailUrl\":\"http://placehold.it/150/56a8c2\"}]";

        // Run
        List<Photo> photosFromServer = FetchDataFromServerIntentService.convertJSONResponseInPhoto(jsonExample);

        // Result
        int amountOfPhotosLoaded = photosFromServer.size();
        assertEquals(amountOfPhotoInJSON, amountOfPhotosLoaded);

    }
}
