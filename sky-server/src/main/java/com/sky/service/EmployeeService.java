package com.sky.service;

import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    Result<String> save(EmployeeDTO employeedto);

    /*
      分页查询
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);
}



/**
 * 这是 **MyBatis 框架的工作机制**，它可以 **自动映射 Java 接口到 SQL 语句**，因此 **你可以在 `Mapper` 接口里定义方法，而不用手动实现它**，然后 MyBatis 会自动执行 `EmployeeMapper.xml` 里的 SQL 语句。
 *
 * ---
 *
 * ## **📌 为什么 `Mapper` 里的方法可以直接调用？**
 * **因为 MyBatis 使用了动态代理（Dynamic Proxy）机制。**
 * 当你的 **Java 接口 (`EmployeeMapper`) 没有具体实现** 时，MyBatis 会在 **运行时** 生成一个 **代理对象** (`Proxy`)，这个代理对象会：
 * 1. **找到 `EmployeeMapper.xml` 里对应 `id` 的 SQL 语句**。
 * 2. **执行 SQL 查询**，并将结果自动封装到指定的 Java 对象（`Employee`）。
 * 3. **返回查询结果** 给调用 `pageQuery()` 方法的地方。
 *
 * ---
 *
 * ## **📌 具体工作流程**
 * ### **1️⃣ 你写了 `EmployeeMapper.java` 接口**
 * ```java
 * public interface EmployeeMapper {
 *     List<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);
 * }
 * ```
 * > **注意：这里没有方法实现！只是一个接口声明**
 *
 * ---
 *
 * ### **2️⃣ 你在 `EmployeeMapper.xml` 里写了 SQL**
 * ```xml
 * <mapper namespace="com.sky.mapper.EmployeeMapper">
 *     <select id="pageQuery" resultType="com.sky.entity.Employee">
 *         select * from employee
 *         <where>
 *             <if test="name != null and name != ''">
 *                 and name like concat('%', #{name}, '%')
 *             </if>
 *         </where>
 *         order by create_time desc
 *     </select>
 * </mapper>
 * ```
 * > **注意：`id="pageQuery"` 与 `EmployeeMapper` 里的方法名相同**
 *
 * ---
 *
 * ### **3️⃣ MyBatis 解析 Mapper**
 * 当应用启动时，MyBatis **会扫描所有 `Mapper.xml` 文件**，并把它们注册到 `SqlSessionFactory` 里。
 *
 * - MyBatis 发现 **`EmployeeMapper.java` 没有实现类**，但它找到了 `EmployeeMapper.xml` 里 **`id="pageQuery"` 的 SQL**。
 * - 于是 **MyBatis 通过动态代理，生成了 `EmployeeMapper` 的一个代理对象**，并把 `pageQuery()` 方法映射到 `EmployeeMapper.xml` 里的 `select` 语句。
 *
 * ---
 *
 * ### **4️⃣ 在 `EmployeeServiceImpl` 里调用**
 * ```java
 * @Service
 * public class EmployeeServiceImpl implements EmployeeService {
 *
 *     @Autowired
 *     private EmployeeMapper employeeMapper;
 *
 *     @Override
 *     public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
 *         List<Employee> employees = employeeMapper.pageQuery(employeePageQueryDTO);
 *         return new PageResult(employees);
 *     }
 * }
 * ```
 * **当 `employeeMapper.pageQuery()` 被调用时：**
 * - **MyBatis 代理对象会找到 `EmployeeMapper.xml` 里的 `pageQuery` SQL**。
 * - **执行 SQL 查询，并自动映射返回的 `ResultSet` 到 `Employee` 对象**。
 * - **返回 `List<Employee>` 结果**。
 *
 * ---
 *
 * ## **📌 结论**
 * 1. **MyBatis 使用 XML 里的 SQL 语句，不需要手写 Mapper 实现类**。
 * 2. **MyBatis 通过动态代理自动执行 SQL，并把查询结果封装成 Java 对象**。
 * 3. **只要接口方法和 XML 里的 `id` 对应，MyBatis 就能自动调用 SQL 并返回数据**。
 *
 * > 这样做的 **好处** 是：
 * > - **降低代码耦合**（SQL 和 Java 逻辑分离）。
 * > - **让 SQL 易于维护**（改 SQL 只需改 XML）。
 * > - **减少冗余代码**（不需要手写 JDBC 代码）。
 *
 * 如果有进一步问题，欢迎继续交流！🚀
 */