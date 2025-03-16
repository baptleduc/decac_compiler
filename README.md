
# Deca Compiler

The Deca compiler is a well-known Software Engineering project I developed during my second year at Ensimag. It compiles Deca, a simplified object-oriented sublanguage of Java, designed to teach the fundamentals of compiler construction. The compiler processes Deca code through lexical analysis, syntax analysis, and code generation, producing assembly code for the IMA (Intermediate Machine Architecture), a virtual machine created for educational purposes. The project also includes scripts to test each phase and ensure the compiler functions correctly.

Originally hosted on GitLab with GitLab CI for continuous integration, the CI tests have not been migrated to GitHub Actions.

## Run the Deca Compiler

To run the Deca compiler, you need to have maven in order to install all dependencies. Then, use the following command in your terminal:

```bash
mvn clean test-compile
./src/main/bin/decac [file-to-compile]
```

**Note**:  
To view the compiler usage, run the following command without any arguments:

```bash
./src/main/bin/decac
```

## Test the Different Phases

### Lexer (Lexical Analysis)

To test the lexical analysis:

```bash
./src/test/script/launchers/test_lex [file-to-test]
```

### Syntax (Syntactic Analysis)

To test the syntax analysis:

```bash
./src/test/script/launchers/test_synt [file-to-test]
```

### Context (Semantic Analysis)

To test the context analysis:

```bash
./src/test/script/launchers/test_context [file-to-test]
```

## Testing with Scripts

The test scripts are available in the `src/test/script` folder. The files used by the scripts are in the `src/test/deca` folder, and the expected results are in the `src/test/results` folder.

## Code Formatting

The code is formatted according to the configuration in the `config/formatter/eclipse-formatter.xml` file. To apply the formatting and pass the CI checks, use the following command:

```bash
mvn spotless:apply
```

## CI Configuration

This project was originally hosted on GitLab, where the CI pipeline was configured using GitLab CI. The CI tests have **not** been migrated to GitHub Actions. Therefore, the continuous integration setup needs to be manually configured on GitHub if desired.
