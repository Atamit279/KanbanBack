package com.example.examenback;

import com.example.examenback.DTO.TaskDTO;
import com.example.examenback.entities.Task;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.BodyInserters;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient(timeout = "PT1M")//30 seconds
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ExamenBackApplicationTests {
    private String serverURL;

    @LocalServerPort
    private int port;

    private final WebTestClient webTestClient;

    @Mock
    private HttpServletRequest request;

    @BeforeAll
    public void setUp(){
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        serverURL = String.format("%s:%s", "localhost", port);

    }


    @Test
    void saveValidTask(){
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("Task1Test");
        taskDTO.setStatus("Todo");

        //act
        TaskDTO savedTask = this.webTestClient
                .post()
                .uri(serverURL + "/api/service/task/")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(BodyInserters.fromValue(taskDTO))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(TaskDTO.class)
                .returnResult()
                .getResponseBody();


        assertNotNull(savedTask);


        HttpStatus deleteUni = this.webTestClient
                .delete()
                .uri(serverURL + "/api/service/task/deleteById/" + savedTask.getId())
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(HttpStatus.class)
                .returnResult()
                .getResponseBody();
    }

    @Test
    void getRequest(){
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("TestTask");
        taskDTO.setStatus("Todo");
        taskDTO.setId(7);

        TaskDTO savedTask = this.webTestClient
                .post()
                .uri(serverURL + "/api/service/task/")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(BodyInserters.fromValue(taskDTO))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(TaskDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(savedTask);

        //act
        TaskDTO getUni = this.webTestClient
                .get()
                .uri(serverURL + "/api/service/task/" + savedTask.getId())
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(TaskDTO.class)
                .returnResult()
                .getResponseBody();

        HttpStatus deleteUni = this.webTestClient
                .delete()
                .uri(serverURL + "/api/service/task/deleteById/" + savedTask.getId())
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(HttpStatus.class)
                .returnResult()
                .getResponseBody();


        assertNotNull(getUni);
        Assertions.assertEquals(savedTask.getId(), getUni.getId());
        Assertions.assertEquals(savedTask.getName(), getUni.getName());
    }

    @Test
    void deleterRequest(){


        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(6);
        taskDTO.setName("TaskDelete");
        taskDTO.setStatus("Todo");

        TaskDTO savedTask = this.webTestClient
                .post()
                .uri(serverURL + "/api/service/task/")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(BodyInserters.fromValue(taskDTO))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(TaskDTO.class)
                .returnResult()
                .getResponseBody();


        //act
        HttpStatus deleteUni = this.webTestClient
                .delete()
                .uri(serverURL + "/api/service/task/deleteById/" + savedTask.getId())
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(HttpStatus.class)
                .returnResult()
                .getResponseBody();


        assertNotNull(savedTask);
    }

    @Test
    void updateRequest(){


        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("TaskUpdate");
        taskDTO.setStatus("Done");

        TaskDTO savedTask = this.webTestClient
                .post()
                .uri(serverURL + "/api/service/task/")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(BodyInserters.fromValue(taskDTO))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(TaskDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(savedTask);
        assertNotNull(savedTask.getId());

        TaskDTO updateDto = new TaskDTO();
        updateDto.setId(savedTask.getId());
        updateDto.setName("Updated-Task");
        updateDto.setStatus("Todo");


        TaskDTO updateTask = this.webTestClient
                .put()
                .uri(serverURL + "/api/service/task/update")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(BodyInserters.fromValue(updateDto))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(TaskDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(updateTask);

        HttpStatus deleteTask = this.webTestClient
                .delete()
                .uri(serverURL + "/api/service/task/deleteById/" + savedTask.getId())
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(HttpStatus.class)
                .returnResult()
                .getResponseBody();


        Assertions.assertNotNull(savedTask);
        Assertions.assertEquals(updateDto.getId(), updateTask.getId());
        Assertions.assertEquals(updateDto.getName(), updateTask.getName());
    }

}
