package com.kernel360.kernelsquare.domain.image.controller;

import com.kernel360.kernelsquare.domain.image.service.ImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import static com.kernel360.kernelsquare.global.common_response.response.code.ImageResponseCode.IMAGE_DELETED;
import static com.kernel360.kernelsquare.global.common_response.response.code.ImageResponseCode.IMAGE_UPLOAD_COMPLETED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("이미지 컨트롤러 통합 테스트")
@WithMockUser
@WebMvcTest(ImageController.class)
class ImageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @Test
    @DisplayName("이미지 업로드 성공시 200 Ok와 응답 메시지를 반환한다.")
    public void uploadImage() throws Exception {
        //given
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "hello.png",
            MediaType.IMAGE_PNG_VALUE,
            "Hello, World!".getBytes()
        );

        given(imageService.uploadImage(anyString(), any(MultipartFile.class))).willReturn("http://example.com/hello.png");

        //when & then
        mockMvc.perform(multipart("/api/v1/images")
                .file(file)
                .param("category", "question")
                .with(csrf())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
            .andExpect(status().is(IMAGE_UPLOAD_COMPLETED.getStatus().value()))
            .andExpect(jsonPath("$.code").value(IMAGE_UPLOAD_COMPLETED.getCode()))
            .andExpect(jsonPath("$.msg").value(IMAGE_UPLOAD_COMPLETED.getMsg()));

        //verify
        verify(imageService, times(1)).uploadImage(anyString(), any(MultipartFile.class));
    }

    @Test
    @DisplayName("이미지 삭제 성공시 200 Ok와 응답 메시지를 반환한다.")
    public void testDeleteImage() throws Exception {
        //given
        String imageUrl = "http://example.com/test.jpg";

        doNothing()
            .when(imageService)
            .deleteImage(imageUrl);

        //when & then
        mockMvc.perform(delete("/api/v1/images")
                .param("imageUrl", imageUrl)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
            .andExpect(status().is(IMAGE_DELETED.getStatus().value()))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(IMAGE_DELETED.getCode()))
            .andExpect(jsonPath("$.msg").value(IMAGE_DELETED.getMsg()));

        //verify
        verify(imageService, times(1)).deleteImage(imageUrl);
    }
}