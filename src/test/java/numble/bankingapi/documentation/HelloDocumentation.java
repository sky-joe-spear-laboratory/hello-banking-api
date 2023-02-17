package numble.bankingapi.documentation;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

class HelloDocumentation extends DocumentationTemplate {

	@Test
	void hello() throws Exception {
		MockHttpServletRequestBuilder builder = RestDocumentationRequestBuilders
			.get("/hello")
			.with(csrf())
			.with(anonymous());


		mockMvc.perform(builder)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andDo(
				document(
					"hello",
					getDocumentRequest(),
					getDocumentResponse(),
					pathParameters()
				)
			);
	}
}
