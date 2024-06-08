package exercise.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import org.instancio.Instancio;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.TaskRepository;
import exercise.model.Task;

// BEGIN
@SpringBootTest
@AutoConfigureMockMvc
// END
class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;


    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring!");
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }


    // BEGIN
    @Test
    public void testShow() throws Exception {
        var result = mockMvc.perform(get("/tasks/" + 1))
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    public void testCreateAndShow() throws Exception {
        Task task = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .ignore(Select.field(Task::getCreatedAt))
                .ignore(Select.field(Task::getUpdatedAt))
                .create();
//        task.setId(8225);
//        task.setCreatedAt(LocalDate.of(2076,7,7));
//        task.setCreatedAt(LocalDate.of(2089,7,15));
        var request = post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(task));

        var response = mockMvc.perform(request)
                .andExpect(status().isCreated()).andReturn().getResponse();
        var json =response.getContentAsString();
        assertThatJson(json).isObject().containsEntry("title", task.getTitle());
        assertThatJson(json).isObject().containsEntry("description", task.getDescription());

        var createdTask = taskRepository.findByTitle(task.getTitle()).get();

        var getResult = mockMvc.perform(get("/tasks/" + createdTask.getId()))
                .andExpect(status().isOk());
    }

//    @Test
//    public void testPut() throws JsonProcessingException {
//        var user = taskRepository.findAll();
//
//        var update = new HashMap<>();
//        update.put("title", faker.lorem().word());
//        update.put("description", faker.lorem().paragraph());
//
//        var request = put("/tasks/" + user.get(0).getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(om.writeValueAsString(update));
//
//        var updatedUser = taskRepository.findById(1L).get();
//
//        assertThat(updatedUser.getTitle().equals(update.get("title")));
//        assertThat(updatedUser.getTitle().equals(update.get("description")));
//    }
    // END
}
