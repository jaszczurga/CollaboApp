package org.example.collaboapp.repository;


import static org.assertj.core.api.Assertions.assertThat;

import org.example.collaboapp.AbstractContainerBasedTest;
import org.example.collaboapp.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleRepositoryTests extends AbstractContainerBasedTest {

        @Autowired
        private RoleRepository roleRepository;

        private Role role;

        @BeforeEach
        public void setUp() {
            this.role = Role.builder()
                    .name( "Test Role")
                    .description( "Test Description")
                    .build();
        }

        @DisplayName("Test for saving a role")
        @Test
        public void givenRoleObject_whenSave_thenReturnSavedRole() {

            Role savedRole = roleRepository.save(role);

            assertThat(savedRole).isNotNull();
            assertThat(savedRole.getRoleId()).isGreaterThan(0);
        }

        @DisplayName("get all roles operation")
        @Test
        public void givenRoleList_whenFindAll_thenRoleList() {
            //given - precoditions for the test

            Role role2 = Role.builder()
                    .name("Test Role2")
                    .build();

            roleRepository.save(role);
            roleRepository.save(role2);

            List<Role> roleList = roleRepository.findAll();

            assertThat(roleList).isNotNull();
            assertThat(roleList.size()).isEqualTo(2);
        }

        @DisplayName("delete role by id operation")
        @Test
        public void givenRoleId_whenDeleteById_thenRoleList() {
            //given - precoditions for the test

            Role savedRole = roleRepository.save(role);

            roleRepository.deleteById(savedRole.getRoleId());

            List<Role> roleList = roleRepository.findAll();

            assertThat(roleList).isNotNull();
            assertThat(roleList.size()).isEqualTo(0);

        }

        @DisplayName("update role operation")
        @Test
        public void givenRoleObject_whenUpdate_thenReturnUpdatedRole() {
            //given - precoditions for the test

            Role savedRole = roleRepository.save(role);

            savedRole.setName("Updated Role");

            Role updatedRole = roleRepository.save(savedRole);

            assertThat(updatedRole).isNotNull();
            assertThat(updatedRole.getRoleId()).isEqualTo(savedRole.getRoleId());
            assertThat(updatedRole.getName()).isEqualTo("Updated Role");

        }

}
