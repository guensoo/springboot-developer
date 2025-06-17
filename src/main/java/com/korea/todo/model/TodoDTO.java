package com.korea.todo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoDTO {
	private String id;
	private String userId; // 이 객체를 생성한 유저의 아이디
	private String title;
	private boolean done;
	
	// 생성자(TodoEntity -> TodoDTO)
	public TodoDTO(TodoEntity entity) {
		this.id = entity.getId();
		this.userId = entity.getUserId();
		this.title = entity.getTitle();
		this.done = entity.isDone();
	}
	
	// TodoDTO -> TodoEntity
	public static TodoEntity toEntity(TodoDTO dto) {
		return TodoEntity.builder()
							.id(dto.getId())
							.userId(dto.getUserId())
							.title(dto.getTitle())
							.done(dto.isDone())
							.build();
	}
}
