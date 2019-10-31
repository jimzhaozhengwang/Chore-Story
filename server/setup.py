#!/usr/bin/env python

from distutils.core import setup

with open('requirements.txt') as req_file:
    required = req_file.read().splitlines()

setup(name='backbone',
      version='1.0',
      description='Server component of chore story',
      author='Mark Keller',
      author_email='markooo.keller@gmail.com',
      packages=['backbone'],
      install_requires=required,
      package_dir={"": "src"},
      )
