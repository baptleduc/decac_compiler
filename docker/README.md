# Docker image corresponding to ensimag environment

Docker is one of the possibilities if you want to work on the project with your personal computer. More informations are available on the [gitlab](https://projet-gl.pages.ensimag.fr/environnement/machine_perso/).

This docker image is proposing an ensimag like environment that can be used to recompile and run JDeca, use ima for your tests and recompile it if needed.

You can either use an existing image available on the ensimag gitlab or rebuild it locally if you want to customize it.

In both cases, when you start the container, you will be in a bash shell on a ubuntu environment in your GL directory hosted by your local operating system. It means that all the project files are both visible from the docker environment and the local operating system

In the docker environment, the user is "gl". This user is sudoer, so that you can manually install new packages if needed (or rebuild a new image adding this package in the Dockerfile for full automation). The "gl" user password (for the sudo command) is "gl".... (no flame !). This can be seen in the Dockerfile.

## Using the existing image

An existing image is available on the ensimag gitlab in the dockergl project. You first have to login on the gitlab docker registry :

```bash
$ docker login gitlab.ensimag.fr:5050
```

You can then create a container (lightweight virtual machine) based on the remote image. The container is named ```projetgl```

```bash
docker create --interactive --tty -v <your GL home dir absolute path>:/home/gl/Projet_GL --name projetgl gitlab.ensimag.fr:5050/reigniep/dockergl
```

 Once the container has been created, you can start it with the following command : 

```bash
docker start -a -i projetgl
```

## Building and running the docker image

Before building the image, you have first to copy ima_sources.tgz from /matieres/4MMPGL/GL/global from an ensimag machine to your local docker directory
To build the image :

```bash
docker build -t projetgl .
```

Once the image has been built, (it may take some time.....), a new container (lightweight virtual machine) can be created with the following command :

```bash
docker create --interactive --tty -v <your GL home dir absolute path>:/home/gl/Projet_GL --name projetgl projetgl
```

This command is in the `new_container` shell script (for linux and mac users) for convenience.  Once the container has been created, you can start it with the following command : 

```bash
docker start -a -i projetgl
```

If you use vs-code, you can configure it to develop inside the container.
See [this page](https://code.visualstudio.com/docs/devcontainers/containers) for instance


