@echo off

set arg1=%1

if %arg1%==startvm (
echo Start VMs

VBoxManage controlvm openstack_controller_2 poweroff
VBoxManage controlvm openstack_compute1_2 poweroff
VBoxManage controlvm openstack_compute2_2 poweroff
VBoxManage controlvm openstack_block1_2 poweroff

VBoxManage snapshot openstack_controller_2 restore syslog
VBoxManage snapshot openstack_compute1_2 restore syslog
VBoxManage snapshot openstack_compute2_2 restore syslog
VBoxManage snapshot openstack_block1_2 restore syslog

VBoxManage startvm openstack_controller_2 --type headless
VBoxManage startvm openstack_compute1_2 --type headless
VBoxmanage startvm openstack_compute2_2 --type headless
VBoxManage startvm openstack_block1_2 --type headless

goto :COMPLETE
)

if %arg1%==poweroff (
echo Power off VMs

VBoxManage controlvm openstack_controller_2 poweroff
VBoxManage controlvm openstack_compute1_2 poweroff
VBoxManage controlvm openstack_compute2_2 poweroff
VBoxManage controlvm openstack_block1_2 poweroff

goto :COMPLETE
)

echo %arg1% is not a Valid Command...
goto :EOF

:COMPLETE
echo Completed...
goto :EOF

:EOF